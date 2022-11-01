package com.shiying.savebox;

import java.util.Objects;

public interface SaveBoxLocker {
    String getPassword();



    void setPassword(String password);

    default boolean checkPassword(String password) {
        return Objects.equals(password, getPassword());
    }

    default boolean isLocked() {
        return getPassword() != null && !getPassword().isEmpty();
    }
}
