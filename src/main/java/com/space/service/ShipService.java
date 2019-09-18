package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.BadRequestException;
import com.space.exception.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShipService {

    @Autowired
    ShipRepository shipRepository;

    public List<Ship> getShipsWithFilters(String name, String planet, ShipType shipType,
                                          Long after,Long before,Boolean isUsed,Double minSpeed,
                                          Double maxSpeed,Integer minCrewSize,Integer maxCrewSize,
                                          Double minRating,Double maxRating){
        List<Ship> ships = shipRepository.findAll();
        List<Ship>fList = new ArrayList<>();
        if(name!=null){
            for(Ship s:ships){
                if(s.getName().contains(name.toLowerCase())){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(planet!=null){
            for(Ship s:ships){
                if(s.getPlanet().contains(planet.toLowerCase())){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(shipType!=null){
            for(Ship s:ships){
                if(s.getShipType().equals(shipType)){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(after!=null){
            for(Ship s:ships){
                if(s.getProdDate().getTime()>after){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(before!=null){
            for(Ship s:ships){
                if(s.getProdDate().getTime()<before){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(isUsed!=null){
            for(Ship s:ships){
                if(s.isUsed().equals(isUsed)){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(minSpeed!=null){
            for(Ship s:ships){
                if(s.getSpeed()>=minSpeed){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(maxSpeed!=null){
            for(Ship s:ships){
                if(s.getSpeed()<=maxSpeed){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(minCrewSize!=null){
            for(Ship s:ships){
                if(s.getCrewSize()>=minCrewSize){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(maxCrewSize!=null){
            for(Ship s:ships){
                if(s.getCrewSize()<=maxCrewSize){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(minRating!=null){
            for(Ship s:ships){
                if(s.getRating()>=minRating){
                    fList.add(s);
                }
            }
            ships=fList;
            fList = new ArrayList<>();
        }
        if(maxRating!=null){
            for(Ship s:ships){
                if(s.getRating()<=maxRating){
                    fList.add(s);
                }
            }
            ships=fList;
        }
        return ships;
    }


    public Ship createShip(Ship ship){
        if(ship==null){
            return null;
        }

        Ship newShip = new Ship();

        if (ship.isUsed() != null) {
            newShip.setUsed(ship.isUsed());
        } else {
            newShip.setUsed(false);
        }
        if(ship.getName()==null||ship.getPlanet()==null||ship.getSpeed()==null||
                ship.getProdDate()==null||ship.getCrewSize()==null||ship.getShipType()==null){
            throw new BadRequestException();
        }

       if(ship.getName().equals("")||ship.getName().length()>50){
        throw new BadRequestException();
    }
       if(ship.getPlanet().equals("")||ship.getPlanet().length()>50){
        throw new BadRequestException();
    }
      if(ship.getProdDate().getTime()<0){
        throw new BadRequestException();
    }
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.set(2800, Calendar.JANUARY, 1);
        maxDate.set(3019, Calendar.DECEMBER, 31);
        if(ship.getProdDate().getTime()<minDate.getTimeInMillis()
                ||ship.getProdDate().getTime()>maxDate.getTimeInMillis()){
            throw new BadRequestException();
        }

        if(ship.getSpeed()<0.01||ship.getSpeed()>0.99){
            throw new BadRequestException();
        }
        if(ship.getCrewSize()<1||ship.getCrewSize()>9999){
            throw new BadRequestException();
        }
        Double rating=calculateRating(ship);

        newShip.setRating(rating);
        newShip.setName(ship.getName());
        newShip.setPlanet(ship.getPlanet());
        newShip.setCrewSize(ship.getCrewSize());
        newShip.setSpeed(ship.getSpeed());
        newShip.setProdDate(ship.getProdDate());
        newShip.setShipType(ship.getShipType());
        return newShip;
    }

    public Ship getShipById(Long id){
        if(id<=0){
            throw new BadRequestException();
        }
        List<Ship>ships= shipRepository.findAll();

        Ship ship = null;
        for(Ship s:ships){
            if(s.getId().equals(id)){
                ship=s;
            }
        }
        if(ship!=null) {
            return ship;
        }else{
            throw new NotFoundException();
        }
    }


    public boolean updateShip(Ship ship,Ship jShip){

        if(jShip.equals(new Ship())){
            return false;
        }
        if(ship!=null) {
            if(jShip.getName()!=null) {
                if (jShip.getName().length() > 50||jShip.getName().equals("")) {
                    throw new BadRequestException();
                } else {
                    ship.setName(jShip.getName());
                }
            }
            if (jShip.getPlanet()!=null) {
                if (jShip.getPlanet().length() > 50 || jShip.getPlanet().equals("")) {
                    throw new BadRequestException();
                } else {
                    ship.setPlanet(jShip.getPlanet());
                }
            }
            if(jShip.getShipType()!=null){
                ship.setShipType(jShip.getShipType());
            }
            if(jShip.getProdDate()!=null) {
                if (jShip.getProdDate().getTime() < 0) {
                    throw new BadRequestException();
                }
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();

                minDate.set(2800, Calendar.JANUARY, 1);
                maxDate.set(3019, Calendar.DECEMBER, 31);
                if (jShip.getProdDate().getTime() < minDate.getTimeInMillis()
                        || jShip.getProdDate().getTime() > maxDate.getTimeInMillis()) {
                    throw new BadRequestException();
                } else {
                    ship.setProdDate(jShip.getProdDate());
                }
            }
            if(jShip.isUsed()!=null) {
                ship.setUsed(jShip.isUsed());
            }
            if(jShip.getSpeed()!=null) {
                if (jShip.getSpeed() < 0.01 || jShip.getSpeed() > 0.99) {
                    throw new BadRequestException();
                }
                ship.setSpeed(jShip.getSpeed());
            }
            if(jShip.getCrewSize()!=null) {
                if (jShip.getCrewSize() < 1 || jShip.getCrewSize() > 9999) {
                    throw new BadRequestException();
                }
                ship.setCrewSize(jShip.getCrewSize());
            }
                ship.setRating(calculateRating(ship));
            return true;
            } else
                {
            return false;
        }
        }

        private Double calculateRating(Ship ship) {
            if (ship.getSpeed() != null && ship.getProdDate() != null) {
                double preRating;
                boolean isUsed = false;
                if (ship.isUsed() != null) {
                    isUsed = ship.isUsed();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ship.getProdDate().getTime());
                int year = calendar.get(Calendar.YEAR);

                if (isUsed) {
                    preRating = 0.5 * 80 * ship.getSpeed() / (3019 - year + 1);
                } else {
                    preRating = 80 * ship.getSpeed() / (3019 - year + 1);
                }
                return (double) Math.round(preRating * 100) / 100;
            } else {
                return null;
            }
        }


    public List<Ship> sortList(List<Ship>ships, Integer pageNumber, Integer pageSize, ShipOrder shipOrder){
        if(pageNumber==null){
            pageNumber=0;
        }
        if(pageSize==null){
            pageSize=3;
        }
        if(shipOrder==null){
            shipOrder=ShipOrder.ID;
        }
            sortByOrder(ships,shipOrder);

        List<Ship>sortedList = new ArrayList<>();
        int size = ships.size();

        if(size%pageSize==0){
            for(int i=pageNumber*pageSize;i<(pageNumber+1)*pageSize;i++){
                sortedList.add(ships.get(i));
            }
        }else {
            int pageCount=(size/pageSize)+1;
            if(pageNumber!=pageCount-1) {
                for (int i = pageNumber * pageSize; i < (pageNumber + 1) * pageSize; i++) {
                    sortedList.add(ships.get(i));
                }
            }else{
                for (int i = pageNumber * pageSize; i < ships.size(); i++) {
                    sortedList.add(ships.get(i));
                }
            }
        }

        return sortedList;
    }


    private void sortByOrder(List<Ship> ships,ShipOrder shipOrder){
        switch (shipOrder){
            case ID:ships.sort((o1, o2) -> (int)(o1.getId()-o2.getId()));
            break;
            case DATE:ships.sort((o1, o2) -> (int)(o1.getProdDate().getTime()-o2.getProdDate().getTime()));
            break;
            case SPEED:ships.sort((o1, o2) -> (int)(100*o1.getSpeed()-100*o2.getSpeed()));
            break;
            case RATING:ships.sort((o1, o2) -> (int)(o1.getRating()-o2.getRating()));
            break;
        }
    }

}
