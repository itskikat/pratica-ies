package com.vogella.spring.issues.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vogella.spring.issues.entities.IssueReport;

public interface IssueRepository extends JpaRepository<IssueReport, Long>{ 
    @Query(value = "SELECT i FROM IssueReport i WHERE markedAsPrivate = false") // allows to define custom JPQL queries that are executed upon calling the method.
    List<IssueReport> findAllButPrivate();

    List<IssueReport> findAllByEmail(String email);
}
// 	leverage Spring data functionality, IssueReport represents the object you are storing and Long the id of the object in the database.