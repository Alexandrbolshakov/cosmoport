package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ShipController {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ShipService shipService;


    @RequestMapping(value = "/rest/ships", method = RequestMethod.GET)
    public List<Ship> getShipsList(
            @RequestParam(required = false)String name,
            @RequestParam(required = false)String planet,
            @RequestParam(required = false)ShipType shipType,
            @RequestParam(required = false)Long after,
            @RequestParam(required = false)Long before,
            @RequestParam(required = false)Boolean isUsed,
            @RequestParam(required = false)Double minSpeed,
            @RequestParam(required = false)Double maxSpeed,
            @RequestParam(required = false)Integer minCrewSize,
            @RequestParam(required = false)Integer maxCrewSize,
            @RequestParam(required = false)Double minRating,
            @RequestParam(required = false)Double maxRating,
            @RequestParam(required = false)ShipOrder order,
            @RequestParam(required = false)Integer pageNumber,
            @RequestParam(required = false)Integer pageSize){

        List<Ship> ships = shipService.getShipsWithFilters(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

                return shipService.sortList(ships, pageNumber,pageSize,order);
    }



    @RequestMapping(value = "/rest/ships/count", method = RequestMethod.GET)
    public Integer getShipsCount(
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String planet,
                         @RequestParam(required = false) ShipType shipType,
                         @RequestParam(required = false) Long after,
                         @RequestParam(required = false) Long before,
                         @RequestParam(required = false) Boolean isUsed,
                         @RequestParam(required = false) Double minSpeed,
                         @RequestParam(required = false) Double maxSpeed,
                         @RequestParam(required = false) Integer minCrewSize,
                         @RequestParam(required = false) Integer maxCrewSize,
                         @RequestParam(required = false) Double minRating,
                         @RequestParam(required = false) Double maxRating) {

        List<Ship> ships = shipService.getShipsWithFilters(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating);
        return ships.size();
    }


    @RequestMapping(value="/rest/ships/", method = RequestMethod.POST)
    public @ResponseBody Ship createShip(@RequestBody Ship jShip){
        Ship ship = shipService.createShip(jShip);
        shipRepository.save(ship);
        return ship;
    }

    @RequestMapping(value = "/rest/ships/{id}", method = RequestMethod.GET)
    public Ship getShip(@PathVariable Long id){
        return shipService.getShipById(id);
    }

    @RequestMapping(value = "/rest/ships/{id}", method = RequestMethod.POST)
    public @ResponseBody Ship updateShip(@RequestBody Ship jShip, @PathVariable Long id){
        Ship ship = shipService.getShipById(id);
        if(shipService.updateShip(ship,jShip)){
            shipRepository.save(ship);
        }
        return ship;
    }
@RequestMapping(value = "/rest/ships/{id}", method = RequestMethod.DELETE)
    public void deleteShip(@PathVariable Long id){
            shipRepository.delete(getShip(id));
    }

    public ShipRepository getShipRepository() {
        return shipRepository;
    }

    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }
}
