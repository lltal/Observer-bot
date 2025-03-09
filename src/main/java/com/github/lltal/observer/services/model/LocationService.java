package com.github.lltal.observer.services.model;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.dto.LocationDto;
import com.github.lltal.observer.output.LocationRepo;
import com.github.lltal.observer.services.builder.LocationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_SENDER_NAME;

@Service
public class LocationService implements ModelService<LocationDto, String> {
    @Autowired
    private LocationRepo repo;
    @Autowired
    private LocationBuilder builder;
    @Autowired
    @Qualifier(LOCATION_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    @Qualifier(LOCATION_SENDER_NAME)
    private AbstractSender sender;
    @Qualifier(LOCATION_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;

    @Override
    public void create(LocationDto dto) {
        repo.save(builder.buildModel(dto));
    }

    @Override
    public LocationDto find(String street) {
        return builder.buildDto(repo.findByStreet(street));
    }

    @Override
    public Collection<LocationDto> findAll() {
        return builder.buildAllDto(repo.findAll());
    }

    public Collection<String> findAllStreets() {
        return repo.findAllStreet();
    }

    @Override
    public void delete(String street) {
        repo.deleteByStreet(street);
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.LOCATION;
    }
}
