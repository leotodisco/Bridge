package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Lavoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LavoroDAO extends JpaRepository<Lavoro, Long> {

}
