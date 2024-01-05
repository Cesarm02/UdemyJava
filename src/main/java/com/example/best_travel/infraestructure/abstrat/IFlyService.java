package com.example.best_travel.infraestructure.abstrat;

import com.example.best_travel.api.models.response.FlyResponse;

import java.util.Set;

public interface IFlyService extends CatalogService<FlyResponse>{

    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);

}
