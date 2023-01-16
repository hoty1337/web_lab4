package com.hoty.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "point")
@NoArgsConstructor
@Getter
@Setter
public class Point {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorPoint")
	@SequenceGenerator(name = "generatorPoint", sequenceName = "points_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "username", nullable = false)
	private String username;
	@Column(name = "x", nullable = false)
	private Double x;
	@Column(name = "y", nullable = false)
	private Double y;
	@Column(name = "r", nullable = false)
	private Double r;
	@Column(name = "result", nullable = false)
	private Boolean result;

	public Point(Double x, Double y, Double r, String username) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.username = username;
	}



}
