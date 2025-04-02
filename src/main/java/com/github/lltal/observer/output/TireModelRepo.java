package com.github.lltal.observer.output;

import com.github.lltal.observer.model.TireModel;
import com.github.lltal.observer.config.constant.enumeration.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TireModelRepo extends JpaRepository<TireModel, Long> {

    TireModel findByName(String name);

    @Query(value = "select trmo.name from tire_model trmo" +
            " join tire_mark trma on trmo.mark_id = trma.id" +
            " where trma.name = :markName and trmo.season = :#{#season?.name()}",
            nativeQuery = true
    )
    Collection<String> findAllNames(
            @Param("markName") String markName,
            @Param("season") Season season
    );

    @Query(value = "delete from tire_model trmo" +
            " where trmo.name = :modelName and trmo.season = :#{#season?.name()}" +
            " and trmo.mark_id in (select tire_mark.id from tire_mark where tire_mark.name = :markName) ",
            nativeQuery = true
    )
    @Modifying(clearAutomatically = true)
    void deleteByNameAndMarkAndSeason(
            @Param("modelName") String modelName,
            @Param("markName") String markName,
            @Param("season") Season season
    );

    @Query(value = "select * from tire_model trmo" +
            " join tire_mark trma on trmo.mark_id = trma.id" +
            " where trma.name = :markName and trmo.name = :modelName and trmo.season = :#{#season?.name()}",
            nativeQuery = true
    )
    TireModel findByNameMarkAndSeason(
            @Param("modelName") String modelName,
            @Param("markName") String markName,
            @Param("season") Season season
    );

    void deleteByName(String name);
}
