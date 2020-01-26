package com.fred.MywebAppWithJPA.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fred.MywebAppWithJPA.Model.Alien;
import com.fred.MywebAppWithJPA.dao.AlienRepo;

@Controller
public class AlienController {
	
	@Autowired
	AlienRepo repo;
	
	
	@RequestMapping("/home2")
	public String home()
	{
		System.out.println("Hi");
		return "home2";
	}

	
	@GetMapping("/addAlien")
	public String addAlien(Alien alien)
	{
		System.out.println("Hi there");
		repo.save(alien);
		return "home2";
	}
	
	@RequestMapping("/get-alien")
	public String getAlien(Alien alien)
	{
		repo.save(alien);
		return "home";
	}
	
	/*Another thing I can do here is find by tech which is not a proper standard of CRUD operation therefore, 
	we cannot use repo.findById() or repo.findAll(); In this case we can create our own method such as findBytech
	which will then return List<> of all the alien with particular technology, say.. java. somethng like below
	*/
	
	@RequestMapping("/getAlien")
	public ModelAndView getAlien(@RequestParam int aid)
	{
		ModelAndView mv =  new ModelAndView("getalien.jsp");
		Alien alien = repo.findById(aid).orElse(new Alien());
		
		
		//We can even write our own query method, for instant, if we are working woth jpq, which is more like sql but with some syntax differences.
		
		//So we can do something like defining our method such as 
		System.out.println(repo.findByTech("Java"));
		System.out.println(repo.findByAidGreaterThan(102));
		System.out.println(repo.findByTechSortedBy("Java"));	
		
		mv.addObject(alien);
		return mv;
		
	}
	
	//We can now move to Webservices using REST: This is mostly used when our resources are consumed by other application rather than human, here, we need to ensure that our resources can be called as localhost:8080/getAlien/102, this way the path is straight forward and we need to render this as json rather than html or xml
	//Below method will find all the aliens in the Aliens table that is created as a result of @Entity in Alien class.
	@RequestMapping(path ="/Aliens", produces= {"application/xml"})//Here we are forcing this to only render xml rather than json
	@ResponseBody
	public List<Alien> getAliens()
	{
		return repo.findAll();
	}
	
	//Another example will be to try and get a single alien: We can achieve this by below method. Our object is converted into json by jackson mavin dependency. What is we want XML then? we need to download the jackson dataformart xml from mvn repository. once we have done this, we then go to postman, under header, we need to enter application/xml
	@RequestMapping("/aliens/{aid}")
	@ResponseBody
	public Optional<Alien> getNewAlien(@PathVariable("aid") int aid)
	{
		return repo.findById(aid);
	}
	
	//Now, we can send data from postman client to the server just the way we can send to server from browser clients. This will follow the same process as completing a form on web page. under Body in the postman, we can enter form by clicking on form, then enter key and value pair. To handle that request though, we have to create a @PostRequest method in our controller
	@PostMapping(path="/Alien", consumes= {"application/json"})//Do this to only consume json data
	@ResponseBody
	public Alien addAnotherAlien(@RequestBody Alien alien)//This @RequestBody will make it work if we enter the raw data in the postman client.
	{
		repo.save(alien);
		return alien;
	}
	
	//Next method is Delete method of CRUD
	@DeleteMapping("/alien/{aid}")
	public Alien deleteAlien(@PathVariable int aid)
	{
		Alien a = repo.getOne(aid);
		 repo.delete(a);
		return a;
		
	}
	
	@PutMapping(path="/Alien", consumes= {"application/json"})//Do this to only consume json data
	public Alien updateAlien(@RequestBody Alien alien)//This @RequestBody will make it work if we enter the raw data in the postman client.
	{
		repo.save(alien);
		return alien;
	}

}
