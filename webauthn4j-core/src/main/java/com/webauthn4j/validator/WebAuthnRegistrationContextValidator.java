/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webauthn4j.validator;


import com.webauthn4j.converter.AttestationObjectConverter;
import com.webauthn4j.converter.AuthenticationExtensionsClientOutputsConverter;
import com.webauthn4j.converter.CollectedClientDataConverter;
import com.webauthn4j.registry.Registry;
import com.webauthn4j.response.WebAuthnRegistrationContext;
import com.webauthn4j.response.attestation.AttestationObject;
import com.webauthn4j.response.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.response.client.ClientDataType;
import com.webauthn4j.response.client.CollectedClientData;
import com.webauthn4j.response.extension.authenticator.ExtensionsAuthenticatorOutputs;
import com.webauthn4j.response.extension.client.AuthenticationExtensionsClientOutputs;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.AssertUtil;
import com.webauthn4j.validator.attestation.AttestationStatementValidator;
import com.webauthn4j.validator.attestation.androidkey.NullAndroidKeyAttestationStatementValidator;
import com.webauthn4j.validator.attestation.androidsafetynet.NullAndroidSafetyNetAttestationStatementValidator;
import com.webauthn4j.validator.attestation.none.NoneAttestationStatementValidator;
import com.webauthn4j.validator.attestation.packed.NullPackedAttestationStatementValidator;
import com.webauthn4j.validator.attestation.trustworthiness.certpath.CertPathTrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.certpath.NullCertPathTrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.ecdaa.ECDAATrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.ecdaa.NullECDAATrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.self.DefaultSelfAttestationTrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.self.NullSelfAttestationTrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.self.SelfAttestationTrustworthinessValidator;
import com.webauthn4j.validator.attestation.u2f.NullFIDOU2FAttestationStatementValidator;
import com.webauthn4j.validator.exception.MaliciousDataException;
import com.webauthn4j.validator.exception.UserNotPresentException;
import com.webauthn4j.validator.exception.UserNotVerifiedException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Validates the specified {@link WebAuthnRegistrationContext} instance
 */
public class WebAuthnRegistrationContextValidator {

    // ~ Instance fields
    // ================================================================================================

    private final Registry registry;

    private final CollectedClientDataConverter collectedClientDataConverter;
    private final AttestationObjectConverter attestationObjectConverter;
    private final AuthenticationExtensionsClientOutputsConverter authenticationExtensionsClientOutputsConverter;

    private final ChallengeValidator challengeValidator = new ChallengeValidator();
    private final OriginValidator originValidator = new OriginValidator();
    private final TokenBindingValidator tokenBindingValidator = new TokenBindingValidator();
    private final RpIdHashValidator rpIdHashValidator = new RpIdHashValidator();
    private final ExtensionValidator extensionValidator = new ExtensionValidator();

    private final AttestationValidator attestationValidator;


    // ~ Constructor
    // ========================================================================================================

    public WebAuthnRegistrationContextValidator(
            List<AttestationStatementValidator> attestationStatementValidators,
            CertPathTrustworthinessValidator certPathTrustworthinessValidator,
            ECDAATrustworthinessValidator ecdaaTrustworthinessValidator,
            SelfAttestationTrustworthinessValidator selfAttestationTrustworthinessValidator
    ) {
        this(attestationStatementValidators,
                certPathTrustworthinessValidator,
                ecdaaTrustworthinessValidator,
                selfAttestationTrustworthinessValidator,
                new Registry());
    }

    public WebAuthnRegistrationContextValidator(
            List<AttestationStatementValidator> attestationStatementValidators,
            CertPathTrustworthinessValidator certPathTrustworthinessValidator,
            ECDAATrustworthinessValidator ecdaaTrustworthinessValidator
    ) {
        this(attestationStatementValidators,
                certPathTrustworthinessValidator,
                ecdaaTrustworthinessValidator,
                new DefaultSelfAttestationTrustworthinessValidator(),
                new Registry());
    }

    public WebAuthnRegistrationContextValidator(
            List<AttestationStatementValidator> attestationStatementValidators,
            CertPathTrustworthinessValidator certPathTrustworthinessValidator,
            ECDAATrustworthinessValidator ecdaaTrustworthinessValidator,
            Registry registry
    ) {
        this(
                attestationStatementValidators,
                certPathTrustworthinessValidator,
                ecdaaTrustworthinessValidator,
                new DefaultSelfAttestationTrustworthinessValidator(),
                registry
        );
    }

    public WebAuthnRegistrationContextValidator(
            List<AttestationStatementValidator> attestationStatementValidators,
            CertPathTrustworthinessValidator certPathTrustworthinessValidator,
            ECDAATrustworthinessValidator ecdaaTrustworthinessValidator,
            SelfAttestationTrustworthinessValidator selfAttestationTrustworthinessValidator,
            Registry registry
    ) {
        AssertUtil.notNull(attestationStatementValidators, "attestationStatementValidators must not be null");
        AssertUtil.notNull(certPathTrustworthinessValidator, "certPathTrustworthinessValidator must not be null");
        AssertUtil.notNull(ecdaaTrustworthinessValidator, "ecdaaTrustworthinessValidator must not be null");
        AssertUtil.notNull(selfAttestationTrustworthinessValidator, "selfAttestationTrustworthinessValidator must not be null");

        this.registry = registry;
        collectedClientDataConverter = new CollectedClientDataConverter(registry);
        attestationObjectConverter = new AttestationObjectConverter(registry);
        authenticationExtensionsClientOutputsConverter = new AuthenticationExtensionsClientOutputsConverter(registry);

        this.attestationValidator = new AttestationValidator(
                attestationStatementValidators,
                certPathTrustworthinessValidator,
                ecdaaTrustworthinessValidator,
                selfAttestationTrustworthinessValidator);
    }


    // ~ Factory methods
    // ========================================================================================================

    public static WebAuthnRegistrationContextValidator createNonStrictRegistrationContextValidator() {
        return createNonStrictRegistrationContextValidator(new Registry());
    }

    public static WebAuthnRegistrationContextValidator createNonStrictRegistrationContextValidator(Registry registry) {
        return new WebAuthnRegistrationContextValidator(
                Arrays.asList(
                        new NoneAttestationStatementValidator(),
                        new NullFIDOU2FAttestationStatementValidator(),
                        new NullPackedAttestationStatementValidator(),
                        new NullAndroidKeyAttestationStatementValidator(),
                        new NullAndroidSafetyNetAttestationStatementValidator()
                ),
                new NullCertPathTrustworthinessValidator(),
                new NullECDAATrustworthinessValidator(),
                new NullSelfAttestationTrustworthinessValidator(),
                registry
        );
    }

    // ~ Methods
    // ========================================================================================================

    public WebAuthnRegistrationContextValidationResponse validate(WebAuthnRegistrationContext registrationContext) {

        BeanAssertUtil.validate(registrationContext);

        byte[] clientDataBytes = registrationContext.getClientDataJSON();
        byte[] attestationObjectBytes = registrationContext.getAttestationObject();

        CollectedClientData collectedClientData = collectedClientDataConverter.convert(clientDataBytes);
        AttestationObject attestationObject = attestationObjectConverter.convert(attestationObjectBytes);
        AuthenticationExtensionsClientOutputs authenticationExtensionsClientOutputs =
                authenticationExtensionsClientOutputsConverter.convert(registrationContext.getClientExtensionsJSON());

        BeanAssertUtil.validate(collectedClientData);
        BeanAssertUtil.validate(attestationObject);
        BeanAssertUtil.validateAuthenticationExtensionsClientOutputs(authenticationExtensionsClientOutputs);

        if(attestationObject.getAuthenticatorData().getAttestedCredentialData() == null){
            throw new MaliciousDataException("attestedCredentialData must not be null on registration");
        }

        byte[] authenticatorDataBytes = attestationObjectConverter.extractAuthenticatorData(attestationObjectBytes);

        RegistrationObject registrationObject = new RegistrationObject(
                collectedClientData,
                clientDataBytes,
                attestationObject,
                attestationObjectBytes,
                authenticatorDataBytes,
                registrationContext.getServerProperty()
        );

        AuthenticatorData authenticatorData = attestationObject.getAuthenticatorData();
        ServerProperty serverProperty = registrationContext.getServerProperty();

        /// Verify that the value of C.type is webauthn.create.
        if (!Objects.equals(collectedClientData.getType(), ClientDataType.CREATE)) {
            throw new MaliciousDataException("Bad client data type");
        }

        /// Verify that the value of C.challenge matches the challenge that was sent to the authenticator in the create() call.
        challengeValidator.validate(collectedClientData, serverProperty);

        /// Verify that the value of C.origin matches the Relying Party's origin.
        originValidator.validate(collectedClientData, serverProperty);

        /// Verify that the value of C.tokenBinding.status matches the state of Token Binding for the TLS connection over
        /// which the assertion was obtained. If Token Binding was used on that TLS connection, also verify that
        /// C.tokenBinding.id matches the base64url encoding of the Token Binding ID for the connection.
        tokenBindingValidator.validate(collectedClientData.getTokenBinding(), serverProperty.getTokenBindingId());

        /// Compute the hash of response.clientDataJSON using SHA-256.

        /// Perform CBOR decoding on the attestationObject field of the AuthenticatorAttestationResponse structure to
        /// obtain the attestation statement format fmt, the authenticator data authData, and the attestation statement attStmt.

        /// Verify that the RP ID hash in authData is indeed the SHA-256 hash of the RP ID expected by the RP.
        rpIdHashValidator.validate(authenticatorData.getRpIdHash(), serverProperty);

        /// If user verification is required for this registration, verify that the User Verified bit of the flags in authData is set.
        if (registrationContext.isUserVerificationRequired() && !authenticatorData.isFlagUV()) {
            throw new UserNotVerifiedException("User not verified");
        }

        /// Verify that the User Present bit of the flags in authData is set.
        if (!registrationContext.isUserVerificationRequired() && !authenticatorData.isFlagUP()) {
            throw new UserNotPresentException("User not present");
        }

        /// Verify that the values of the client extension outputs in clientExtensionResults and the authenticator
        /// extension outputs in the extensions in authData are as expected, considering the client extension input
        /// values that were given as the extensions option in the create() call. In particular, any extension identifier
        /// values in the clientExtensionResults and the extensions in authData MUST be also be present as extension
        /// identifier values in the extensions member of options, i.e., no extensions are present that were not requested.
        /// In the general case, the meaning of "are as expected" is specific to the Relying Party and which extensions are in use.
        ExtensionsAuthenticatorOutputs extensionsAuthenticatorOutputs = authenticatorData.getExtensions();
        List<String> expectedExtensionIdentifiers = registrationContext.getExpectedExtensionIds();
        extensionValidator.validate(authenticationExtensionsClientOutputs, extensionsAuthenticatorOutputs, expectedExtensionIdentifiers);

        // Verify attestation
        attestationValidator.validate(registrationObject);

        // If the attestation statement attStmt verified successfully and is found to be trustworthy,
        // then register the new credential with the account that was denoted in the options.user passed to create(),
        // by associating it with the credential ID and credential public key contained in authData’s attestation data,
        // as appropriate for the Relying Party's systems.

        /// Check that the credentialId is not yet registered to any other user. If registration is requested for
        /// a credential that is already registered to a different user, the Relying Party SHOULD fail this registration
        /// ceremony, or it MAY decide to accept the registration, e.g. while deleting the older registration.

        // ******* This step is up to library user *******

        return new WebAuthnRegistrationContextValidationResponse(collectedClientData, attestationObject, authenticationExtensionsClientOutputs);
    }


}
