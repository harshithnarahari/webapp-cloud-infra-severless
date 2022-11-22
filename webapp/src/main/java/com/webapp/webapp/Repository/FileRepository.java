package com.webapp.webapp.Repository;

import com.webapp.webapp.Model.Docu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Docu, Long> {

    Docu findByName(String name);


}
