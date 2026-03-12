package ch.bbcag.wrodit.dto.request;

import ch.bbcag.wrodit.security.annotation.Password;
import ch.bbcag.wrodit.security.annotation.Username;

public record UserRequestDTO(@Username String name, @Password String password) {}
