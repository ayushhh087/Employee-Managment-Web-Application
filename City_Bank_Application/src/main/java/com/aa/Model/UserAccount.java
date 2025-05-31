package com.aa.Model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.NonFinal;

@Entity
@Getter
@Setter
@Table(name = "UserAccount_Tab")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor

public class UserAccount {
	
	@Id
	@SequenceGenerator(name = "g",initialValue = 100,allocationSize = 1)
	@GeneratedValue(generator = "g",strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name="Name",length = 30)
	@NonNull
	private String name;
	
	@Column(name="Username", length = 20,unique = true)
	@NonNull
	private String username;
	
	@Column(name = "Email", length = 40)
	@NonNull
	private String email;
	
	@Column(name="Phone",length = 13)
	@NonNull
	private String phone;
	
	@Column(name = "Password", length = 65)
	@NonNull
	private String password;
	
	@Column(name = "Balance")
	@NonNull
	private Double balance = 0.0;
	
	@Column(name = "Active")
	private boolean isActive = true;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="SECURITY_ROLES", joinColumns = @JoinColumn(name="USER_ID"))
	@Column(name = "role" ,updatable = true, length = 30)
	private Set<String> roles;
	
	@OneToMany(targetEntity = TransactionHistory.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	private Set<TransactionHistory> transactions;

	@Override
	public String toString() {
		return "UserAccount [id=" + id + ", name=" + name + ", username=" + username + ", email=" + email + ", phone="
				+ phone + ", password=" + password + ", balance=" + balance + ", isActive=" + isActive + "]";
	}
	
	
}
