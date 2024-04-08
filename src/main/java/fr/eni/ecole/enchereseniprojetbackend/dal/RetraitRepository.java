package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetraitRepository extends JpaRepository<Retrait, Long> {
}
