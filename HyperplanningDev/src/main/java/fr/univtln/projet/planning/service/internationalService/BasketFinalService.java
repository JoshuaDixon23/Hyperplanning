package fr.univtln.projet.planning.service.internationalService;


import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.internationalRepository.BasketFinalRepository;

import java.util.Optional;

public class BasketFinalService {

    private final BasketFinalRepository basketFinalRepository;

    public BasketFinalService(BasketFinalRepository basketFinalRepository) {
        this.basketFinalRepository = basketFinalRepository;
    }

    // création panier
    public BasketFinal createBasket() {
        return basketFinalRepository.save(new BasketFinal());
    }


    public BasketFinalRepository getBasketFinalRepository() {
        return basketFinalRepository;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    // obtenir un panier
    public BasketFinal getBasket(Long id) {
        return basketFinalRepository.findWithAll(id).orElse(null);
    }

    // ajout de module
    public BasketFinal addModule(Long id, Module module, GroupEntity group) {
        Optional<BasketFinal> opt = basketFinalRepository.findWithAll(id);
        if (opt.isEmpty()) return null;

        BasketFinal basket = opt.get();
        basket.addModuleGroup(module, group);

        return basketFinalRepository.save(basket);
    }

    // suppression de module
    public BasketFinal removeModule(Long id, ModuleEntity module) {
        Optional<BasketFinal> opt = basketFinalRepository.findWithAll(id);
        if (opt.isEmpty()) return null;

        BasketFinal basket = opt.get();
        basket.removeModule(module);

        return basketFinalRepository.save(basket);
    }

}