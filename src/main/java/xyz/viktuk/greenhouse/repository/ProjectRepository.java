package xyz.viktuk.greenhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.viktuk.greenhouse.entity.Project;

import java.math.BigInteger;

@Repository
public interface ProjectRepository extends JpaRepository<Project, BigInteger> {
}
