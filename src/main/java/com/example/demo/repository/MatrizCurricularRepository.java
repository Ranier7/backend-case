package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.MatrizCurricular;

@Repository
public interface MatrizCurricularRepository extends JpaRepository<MatrizCurricular, Long> {

}
