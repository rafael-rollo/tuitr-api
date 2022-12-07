package br.com.rollo.rafael.tuitrapi.domain.users;

import java.time.LocalDate;

public interface UpdatableUserInfo {
    String getUsername();
    String getFullName();
    String getProfilePicturePath();
    LocalDate getBirthDate();
    String getLocation();
}
