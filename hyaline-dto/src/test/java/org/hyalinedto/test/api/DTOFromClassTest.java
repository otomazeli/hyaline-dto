package org.hyalinedto.test.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.hyalinedto.api.DTO;
import org.hyalinedto.api.Hyaline;
import org.hyalinedto.api.HyalineDTO;
import org.hyalinedto.exception.HyalineException;
import org.hyalinedto.test.annotations.TestFieldAnnotation;
import org.hyalinedto.test.annotations.TestFieldAnnotationWithAnnotationMember;
import org.hyalinedto.test.domainclasses.Address;
import org.hyalinedto.test.domainclasses.Person;
import org.junit.Before;
import org.junit.Test;

public class DTOFromClassTest {

	private org.hyalinedto.test.domainclasses.Person john;

	@Before
	public void init() {
		john = new Person();
		john.setFirstName("John");
		john.setLastName("Lennon");

		Address addr = new Address();
		addr.setStreet("Abbey Road");
		addr.setNumber(123);
		addr.setZipcode("NW8 9AX");
		addr.setCity("London");
		addr.setCountry("UK");

		john.setAddress(addr);
	}

	@Test
	public void testFieldAnnotationAdded() throws HyalineException,
			NoSuchFieldException, SecurityException {
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			@TestFieldAnnotation
			private String firstName;

		});
		Field fn = dto.getClass().getDeclaredField("firstName");
		TestFieldAnnotation annotation = fn
				.getAnnotation(TestFieldAnnotation.class);
		assertNotNull(annotation);
	}
	
	@Test
	public void testFieldAnnotationWithMemberAnnotationAdded() throws HyalineException,
			NoSuchFieldException, SecurityException {
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			@TestFieldAnnotationWithAnnotationMember(testAnnotation = @TestFieldAnnotation(intValue = 123))
			private String firstName;

		});
		Field fn = dto.getClass().getDeclaredField("firstName");
		TestFieldAnnotationWithAnnotationMember outer = fn
				.getAnnotation(TestFieldAnnotationWithAnnotationMember.class);
		TestFieldAnnotation testAnnotation = outer.testAnnotation();
		assertNotNull(outer);
		assertNotNull(testAnnotation);
		assertEquals(123, testAnnotation.intValue());
		
	}

	@Test
	public void testClassnameAssigned() throws HyalineException {
		final String proxyClassName = "org.hyalinedto.MyClass2";
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			@TestFieldAnnotation
			private String firstName;

		}, proxyClassName);
		assertEquals(proxyClassName, dto.getClass().getName());
	}
	
	
	@Test
	public void testFieldValueOverwritten() throws HyalineException{
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			@SuppressWarnings("unused")
			private String firstName = "Ringo";

		});
		assertEquals("Ringo", dto.getFirstName());
	}
	
	@Test
	public void testWithArrayField() throws HyalineException{
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			
			@SuppressWarnings("unused")
			private String[] colors = {"Black"};

		});
		assertEquals("Black", dto.getColors()[0]);
	}
	
	@Test
	public void testAccessToNewField() throws HyalineException{
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			@SuppressWarnings("unused")
			private String name = john.getFirstName();

		});
		HyalineDTO hyalineDTO = (HyalineDTO) dto;
		
		assertEquals(hyalineDTO.getAttribute("name"), john.getFirstName());

	}
	
	@Test
	public void tenstAnnotationOverWritten() throws HyalineException, NoSuchFieldException, SecurityException {
		final Person dto = Hyaline.dtoFromClass(john, new DTO() {

			@TestFieldAnnotation(intValue = 3)
			private Address address;
			
		});
		Field field = dto.getClass().getDeclaredField("address");
		TestFieldAnnotation annotation = field.getAnnotation(TestFieldAnnotation.class);
		assertEquals(3, annotation.intValue());
	}
	
}