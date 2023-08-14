package com.cavdar.employeemanagement.domain.assembler;

import com.cavdar.employeemanagement.controller.AuthController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDetailsModelAssembler implements RepresentationModelAssembler<UserDetails, EntityModel<UserDetails>> {
    @Override
    public EntityModel<UserDetails> toModel(UserDetails entity) {
        // TODO: 7/21/23 Add links to user it self
        return EntityModel.of(
                entity
        );
    }
}
