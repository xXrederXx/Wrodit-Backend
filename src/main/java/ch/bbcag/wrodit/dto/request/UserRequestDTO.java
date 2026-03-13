package ch.bbcag.wrodit.dto.request;

import ch.bbcag.wrodit.util.annotation.Password;
import ch.bbcag.wrodit.util.annotation.Username;

public record UserRequestDTO(@Username String username, @Password String password) {}
