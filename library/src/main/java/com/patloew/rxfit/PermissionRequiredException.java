package com.patloew.rxfit;

public class PermissionRequiredException extends Throwable {
    private final String permission;

    PermissionRequiredException(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
