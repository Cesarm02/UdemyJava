package com.example.best_travel.infraestructure.abstrat;

public interface SimpleCrudService<RQ, RS, ID>{

    RS create(RQ request);

    RS read(ID id);

    void delete(ID id);

}
