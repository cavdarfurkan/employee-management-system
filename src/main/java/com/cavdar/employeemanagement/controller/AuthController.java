package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.assembler.UserDetailsModelAssembler;
import com.cavdar.employeemanagement.domain.model.Authority;
import com.cavdar.employeemanagement.domain.model.RefreshToken;
import com.cavdar.employeemanagement.domain.model.User;
import com.cavdar.employeemanagement.domain.repository.AuthorityRepository;
import com.cavdar.employeemanagement.domain.service.RefreshTokenService;
import com.cavdar.employeemanagement.domain.service.UserService;
import com.cavdar.employeemanagement.payload.request.LoginRequest;
import com.cavdar.employeemanagement.payload.request.TokenRefreshRequest;
import com.cavdar.employeemanagement.payload.response.JwtResponse;
import com.cavdar.employeemanagement.payload.response.MessageResponse;
import com.cavdar.employeemanagement.payload.response.TokenRefreshResponse;
import com.cavdar.employeemanagement.util.JwtTokenProvider;
import com.cavdar.employeemanagement.util.exception.RefreshTokenException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final UserDetailsModelAssembler userDetailsModelAssembler;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService,
                          UserService userService, UserDetailsModelAssembler userDetailsModelAssembler, AuthorityRepository authorityRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.userDetailsModelAssembler = userDetailsModelAssembler;
        this.authorityRepository = authorityRepository;
    }

    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (this.userService.isExistsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }

        if (this.userService.isExistsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
        }

        EntityModel<UserDetails> entityModel = userDetailsModelAssembler.toModel(this.userService.saveUserAndEncryptPassword(user));

        return ResponseEntity
                .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(entityModel);
    }

    /**
     * The `processLogin` function handles the login process by authenticating the user, generating a JWT
     * token, creating a refresh token, and returning the tokens along with the user details in the
     * response.
     *
     * @param loginRequest The `loginRequest` parameter is an object of type `LoginRequest`. It is
     *                     annotated with `@RequestBody`, which means that it will be deserialized from the request body of the
     *                     HTTP POST request.
     * @return The method is returning a ResponseEntity object. If the authentication is successful, it
     * returns a ResponseEntity with a status of OK and a body containing a JwtResponse object. If the
     * authentication fails, it returns a ResponseEntity with a status of UNAUTHORIZED and a body
     * containing a MessageResponse object.
     */
    @PostMapping("/login")
    public ResponseEntity<?> processLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);

            User userDetails = (User) authentication.getPrincipal();
            EntityModel<UserDetails> entityModel = userDetailsModelAssembler.toModel(userDetails);

            refreshTokenService.deleteByUserId(userDetails.getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok().body(
                    new JwtResponse(token, refreshToken.getToken(), entityModel));
        } catch (AuthenticationException e) {
            logger.error("Exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password"));
        }
    }

    /**
     * The `refreshToken` function in Java handles the refreshing of access tokens by verifying the
     * expiration of the refresh token and generating a new access token.
     *
     * @param request The `request` parameter is an object of type `TokenRefreshRequest`. It is annotated
     *                with `@Valid` to indicate that it should be validated before processing. The `TokenRefreshRequest`
     *                class likely contains fields for the refresh token, which is obtained from the request body.
     * @return The method is returning a ResponseEntity object. If the refresh token is valid and not
     * expired, it returns a ResponseEntity with HTTP status code 200 (OK) and a body containing a
     * TokenRefreshResponse object, which includes a new access token and the refresh token. If the refresh
     * token is invalid or expired, it returns a ResponseEntity with HTTP status code 403 (FORBIDDEN) and a
     * body containing
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        try {
            RefreshToken refreshToken = this.refreshTokenService.findByToken(request.getRefreshToken());
            refreshToken = this.refreshTokenService.verifyExpiration(refreshToken);

            String token = jwtTokenProvider.generateTokenFromUsername(refreshToken.getUser().getUsername());

            return ResponseEntity
                    .ok()
                    .body(new TokenRefreshResponse(token, refreshToken.getToken()));
        } catch (RefreshTokenException e) {
            logger.error("Exception: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * The above function processes a logout request by deleting the refresh token associated with the
     * given user ID.
     *
     * @param id The `id` parameter is of type `Long` and is used to identify the user for whom the refresh
     *           token needs to be deleted.
     */
    @PostMapping("/logout")
    public void processLogout(@RequestParam Long id) {
        refreshTokenService.deleteByUserId(id);
    }

    /**
     * The above function returns a collection of all roles with links to itself.
     *
     * @return The method is returning a CollectionModel of EntityModel objects representing authorities.
     */
    @GetMapping("/all-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<EntityModel<Authority>> getAllRoles() {
        return CollectionModel.of(this.authorityRepository.findAll().stream()
                        .map(EntityModel::of).toList(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).getAllRoles()).withSelfRel());
    }
}
