package cpu_jwt.services.security;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    /* UserDetails contient les informations nécessaires pour créer
     un objet d'authentification à partir de DAO ou d'une autre source de données de sécurité.
   cette déclaration de méthode définit une méthode appelée
    userDetailsService qui retourne un objet de type UserDetailsService.*/

    UserDetailsService userDetailsService();
}