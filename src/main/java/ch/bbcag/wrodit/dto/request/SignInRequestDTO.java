package ch.bbcag.wrodit.dto.request;

import ch.bbcag.wrodit.util.annotation.Password;
import ch.bbcag.wrodit.util.annotation.Username;

public record SignInRequestDTO(@Username String username, @Password String password) {}
