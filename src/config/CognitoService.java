package src.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;

public class  CognitoService {
    private  final AWSCognitoIdentityProvider cognitoClient;
    private  final String clientId;
    private  final String userPoolId;

    public  SignUpResult registerUser(String username, String password, String email) {
        SignUpRequest signUpRequest = new SignUpRequest()
                .withClientId(clientId)
                .withUsername(email)
                .withPassword(password);


        return cognitoClient.signUp(signUpRequest);
    }

    public ConfirmSignUpResult confirmUser(String username, String confirmationCode) {
        ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
                .withClientId(clientId)
                .withUsername(username)
                .withConfirmationCode(confirmationCode);

        return cognitoClient.confirmSignUp(confirmSignUpRequest);
    }

    public CognitoService(String accessKey, String secretKey, String region, String clientId, String userPoolId) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.fromName(region))
                .build();
        this.clientId = clientId;
        this.userPoolId = userPoolId;
    }

    public AuthenticationResultType authenticate(String username, String password) {
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(clientId)
                .addAuthParametersEntry("USERNAME", username)
                .addAuthParametersEntry("PASSWORD", password);

        InitiateAuthResult authResult = cognitoClient.initiateAuth(authRequest);
        return authResult.getAuthenticationResult();
    }
}
