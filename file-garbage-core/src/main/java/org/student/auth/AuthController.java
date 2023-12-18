package org.student.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public String admin() {
		return "Admin endpoint";
	}

	@GetMapping("/super_admin")
	@PreAuthorize("hasAuthority('SCOPE_SUPERADMIN')")
	public String superAdmin() {
		return "Super Admin endpoint";
	}

}
