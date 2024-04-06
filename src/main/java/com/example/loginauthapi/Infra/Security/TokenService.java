package com.example.loginauthapi.Infra.Security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.loginauthapi.Utils.ExpireAtUtil;
import com.example.loginauthapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//Responsavel por criar e validar token jwt de um usuário especifico.
@Service
public class TokenService {

    @Autowired
    private ExpireAtUtil expireAtUtil;
    @Value("${api.secret.key.algorithm}")
    private String secret_key_algorithm;

   ;
    public String generatedToken(User userRequest) throws Exception {
        try {
            //Definindo algoritimo de criptagrafia
            //.withIssuer = Quem esta emitindo o token.
            //.withSubject = Quem esta obtendo esse token, no caso o usuário;
            //.withExpiresAt = Calculo de quando o token sera expirado.
            //.sign = Indica o algoritimo que sera usado na criação do token
             Algorithm algorithm =  Algorithm.HMAC256(secret_key_algorithm);

            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(userRequest.getEmail())
                    .withExpiresAt(this.expireAtUtil.gerateExpiretionDate())
                    .sign(algorithm);
            return token;


        }catch (JWTCreationException exception) {
            throw new RuntimeException("Erro while gerenated a token");
        }
    }
    public String validateToken(String token) {
        try {
             Algorithm algorithm =  Algorithm.HMAC256(secret_key_algorithm);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
           return null;
        }
    }
}
