package fr.univtln.projet.planning.service.internationalService;

import fr.univtln.projet.planning.modele.international.BasketModule;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.repository.internationalRepository.BasketModuleRepository;

import java.util.Optional;

public class BasketModuleService {

    private final BasketModuleRepository basketModuleRepository;

    public BasketModuleService(BasketModuleRepository basketModuleRepository) {
        this.basketModuleRepository = basketModuleRepository;
    }

    // créer un panier pour un étudiant
    public BasketModule createBasket(InternationalStudentEntity student) {
        BasketModule basket = new BasketModule();
        basket.setInternationalStudent(student);
        return basketModuleRepository.save(basket);
    }

    // récupérer le panier d’un étudiant
    public BasketModule getBasket(InternationalStudentEntity student) {
        return basketModuleRepository.findByStudent(student).orElse(null);
    }


}