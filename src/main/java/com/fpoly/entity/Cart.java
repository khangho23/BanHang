package com.fpoly.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "quantity")
	private int quantity;
	
	@Column(name = "createdat")
	private Date createdAt = new Date();
	
	@Column(name = "productdetailid")
	private Integer productDetailId;
	
	@Column(name = "userid")
	private int userId;

	@Column(name = "orderid")
	private int orderId;

	@Transient
	public double getTotal() {
		return productDetail.getPrice() * quantity;
	}

	@ManyToOne
	@JoinColumn(name = "productdetailid", insertable = false, updatable = false)
	@JsonIgnore
	ProductDetail productDetail;

	@ManyToOne
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    @JsonIgnore
    Order order;
}
