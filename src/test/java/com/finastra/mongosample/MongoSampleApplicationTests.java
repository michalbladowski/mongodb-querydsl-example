package com.finastra.mongosample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finastra.mongosample.model.User;
import com.finastra.mongosample.model.QUser;
import com.finastra.mongosample.repository.UserRepository;
import com.finastra.mongosample.service.UserService;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
class MongoSampleApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MongoOperations operations;
	@Autowired
	private UserRepository userRepository;
	@MockBean
	private UserService userService;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();

		User user1 = new User();
		user1.setUserName("John");
		user1.setAge(31);
		user1.setOccupation("lawyer");
		User user2 = new User();
		user2.setUserName("Bob");
		user2.setAge(55);
		user2.setOccupation("plumber");
		User user3 = new User();
		user3.setUserName("Alice");
		user3.setAge(25);
		user3.setOccupation("student");
		User user4 = new User();
		user4.setUserName("Steve");
		user4.setAge(40);
		user4.setOccupation("plumber");

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);
	}

	@Test
	void addUser() throws Exception {
		User user = new User();
		user.setUserName("Wilson");
		user.setCreationDate(new Date());
		String body = mapper.writeValueAsString(user);

		mockMvc.perform(post("/users/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void findAllByPredicate_Name() {
		QUser qUser = new QUser("test");
		Predicate predicate = qUser.userName.eq("Alice");
		assertEquals(1, userRepository.findAll(predicate).spliterator().getExactSizeIfKnown());
	}

	@Test
	void findAllByPredicate_Age() {
		QUser qUser = new QUser("test");
		Predicate predicate = qUser.age.gt(30);
		assertEquals(3, userRepository.findAll(predicate).spliterator().getExactSizeIfKnown());
	}

	@Test
	void findAllByPredicate_CertainAgeNotWithNameBob() {
		QUser qUser = new QUser("test");
		Predicate predicate = qUser.age.eq(55)
				.and(qUser.userName.eq("Bob").not());
		assertEquals(0, userRepository.findAll(predicate).spliterator().getExactSizeIfKnown());
	}

	@Test
	void findAllByPredicate_CertainOccupationAndName() {
		QUser qUser = new QUser("test");
		Predicate predicate = qUser.occupation.eq("plumber")
				.and(qUser.age.lt(45));
		assertEquals(1, userRepository.findAll(predicate).spliterator().getExactSizeIfKnown());
	}

	@Test
	void findAllByAgePredicateAndInAscOrder() {
		QUser qUser = new QUser("test");
		BooleanExpression booleanExpression = qUser.age.goe(31);
		OrderSpecifier<Integer> orderSpecifier = qUser.age.asc();
		Iterable<User> users = userRepository.findAll(booleanExpression, orderSpecifier);
		assertEquals(3, users.spliterator().getExactSizeIfKnown());
	}

}
