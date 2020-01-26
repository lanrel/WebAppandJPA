package com.fred.MywebAppWithJPA.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.fred.MywebAppWithJPA.Model.Alien;

//public interface AlienRepo extends CrudRepository<Alien, Integer> is used for CRUD but JpaRpository is better and can also be used for CRUD operations but with extra features
public interface AlienRepo extends JpaRepository<Alien, Integer>
{
	List<Alien> findByTech(String tech);
	List<Alien> findByAidGreaterThan(int aid);
	
	@Query("from Alien where name = ?1 order by aname")
	List<Alien> findByTechSortedBy(String tech);
}
