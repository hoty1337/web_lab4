package com.hoty.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@SequenceGenerator(name = "generatorU", sequenceName = "users_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorU")
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "username", nullable = false)
	private String username;
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	@Column(name = "password_salt", nullable = false)
	private String passwordSalt;

	public User(String username, String passwordHash, String passwordSalt) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
	}
}
