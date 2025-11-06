package controladores;

import dtos.JwtAuthResponseDTO;
import dtos.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import config.JwtTokenProvider; // <-- El generador que creamos
import java.util.Map; // Para el error

@RestController
@RequestMapping("/auth") // El prefijo para este controlador
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * POST /auth/login
     * Este es el endpoint que el front-end (login.js) llamará.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {

        try {
            // 1. Spring Security intenta autenticar al usuario
            //    Usará nuestro AppUserDetailsService y el PasswordEncoder
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // 2. Si tiene éxito, creamos el token JWT
            String jwt = tokenProvider.generateToken(authentication);

            // 3. Devolvemos el token al front-end
            return ResponseEntity.ok(new JwtAuthResponseDTO(jwt));

        } catch (Exception e) {
            // Si las credenciales son incorrectas, el AuthenticationManager
            // lanzará una excepción y caeremos aquí.
            return ResponseEntity.status(401) // 401 Unauthorized
                           .body(Map.of("error", "Email o contraseña incorrectos."));
        }
    }
}