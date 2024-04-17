package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;
import fr.eni.ecole.enchereseniprojetbackend.bll.RetraitService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import org.springframework.stereotype.Service;



@Service
public class RetraitServicesImpl  implements RetraitService {

    final RetraitRepository retraitRepository;

    public RetraitServicesImpl(RetraitRepository retraitRepository) {
        this.retraitRepository = retraitRepository;
    }

    public void deleteRetrait(long id){
        retraitRepository.deleteById(id);
    }

    public Retrait recupererRetraitById(long id){
        return retraitRepository.getReferenceById(id);
    }
}