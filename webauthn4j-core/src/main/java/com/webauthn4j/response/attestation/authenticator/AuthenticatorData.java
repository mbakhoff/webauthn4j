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

package com.webauthn4j.response.attestation.authenticator;

import com.webauthn4j.response.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class AuthenticatorData implements Serializable {
    public static final byte BIT_UP = (byte) 0b00000001;
    public static final byte BIT_UV = (byte) 0b00000100;
    public static final byte BIT_AT = (byte) 0b01000000;
    public static final byte BIT_ED = (byte) 0b10000000;

    private final byte[] rpIdHash;
    private final byte flags;
    private final long signCount;
    private final AttestedCredentialData attestedCredentialData;
    private final AuthenticationExtensionsAuthenticatorOutputs extensions;

    public AuthenticatorData(byte[] rpIdHash, byte flags, long counter,
                             AttestedCredentialData attestedCredentialData,
                             AuthenticationExtensionsAuthenticatorOutputs extensions) {
        this.rpIdHash = rpIdHash;
        this.flags = flags;
        this.signCount = counter;
        this.attestedCredentialData = attestedCredentialData;
        this.extensions = extensions;
    }

    public AuthenticatorData(byte[] rpIdHash, byte flags, long counter,
                             AttestedCredentialData attestedCredentialData) {
        this.rpIdHash = rpIdHash;
        this.flags = flags;
        this.signCount = counter;
        this.attestedCredentialData = attestedCredentialData;
        this.extensions = new AuthenticationExtensionsAuthenticatorOutputs();
    }

    public AuthenticatorData(byte[] rpIdHash, byte flags, long counter,
                             AuthenticationExtensionsAuthenticatorOutputs extensions) {
        this.rpIdHash = rpIdHash;
        this.flags = flags;
        this.signCount = counter;
        this.attestedCredentialData = null;
        this.extensions = extensions;
    }

    public AuthenticatorData(byte[] rpIdHash, byte flags, long counter) {
        this.rpIdHash = rpIdHash;
        this.flags = flags;
        this.signCount = counter;
        this.attestedCredentialData = null;
        this.extensions = new AuthenticationExtensionsAuthenticatorOutputs();
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean checkFlagUP(byte flags) {
        return (flags & BIT_UP) != 0;
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean checkFlagUV(byte flags) {
        return (flags & BIT_UV) != 0;
    }

    public static boolean checkFlagAT(byte flags) {
        return (flags & BIT_AT) != 0;
    }

    public static boolean checkFlagED(byte flags) {
        return (flags & BIT_ED) != 0;
    }

    public byte[] getRpIdHash() {
        return rpIdHash;
    }

    public byte getFlags() {
        return flags;
    }

    public boolean isFlagUP() {
        return checkFlagUP(this.flags);
    }

    public boolean isFlagUV() {
        return checkFlagUV(this.flags);
    }

    public boolean isFlagAT() {
        return checkFlagAT(this.flags);
    }

    public boolean isFlagED() {
        return checkFlagED(this.flags);
    }

    public long getSignCount() {
        return signCount;
    }

    public AttestedCredentialData getAttestedCredentialData() {
        return attestedCredentialData;
    }

    public AuthenticationExtensionsAuthenticatorOutputs getExtensions() {
        return extensions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticatorData that = (AuthenticatorData) o;
        return flags == that.flags &&
                signCount == that.signCount &&
                Arrays.equals(rpIdHash, that.rpIdHash) &&
                Objects.equals(attestedCredentialData, that.attestedCredentialData) &&
                Objects.equals(extensions, that.extensions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(flags, signCount, attestedCredentialData, extensions);
        result = 31 * result + Arrays.hashCode(rpIdHash);
        return result;
    }
}
