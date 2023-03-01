package es.uma.informatica.sii.helloworld.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uma.informatica.sii.helloworld.entities.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}
