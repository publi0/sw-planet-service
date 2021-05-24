package com.starwars.planets.service;

import com.starwars.planets.PlanetProperties;
import com.starwars.planets.dto.FoundPlanetIntegration;
import com.starwars.planets.exception.ConflictException;
import com.starwars.planets.exception.DataNotFoundException;
import com.starwars.planets.model.Planet;
import com.starwars.planets.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.starwars.planets.utils.TestUtils.getRandomInteger;
import static com.starwars.planets.utils.TestUtils.getRandomString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

	@Mock
	private PlanetProperties propertiesMock;

	@Mock
	private PlanetRepository planetRepositoryMock;

	@Mock
	private StarWarsIntegration starWarsIntegrationMock;

	@InjectMocks
	private PlanetService planetService;

	@Captor
	private ArgumentCaptor<Planet> planetArgumentCaptor;

	@Test
	public void createPlanet_shouldCreatePlanet() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var incomePlanet = new Planet(planetName, planetClimate, planetTerrain);
		final var outPlanet = new Planet(getRandomString(), UUID.randomUUID()
				.toString(), planetName, planetClimate, planetTerrain, LocalDateTime.now(), "user");
		final var applicationName = "ApplicationName";

		when(planetRepositoryMock.findByNameIgnoreCase(incomePlanet.getName())).thenReturn(Optional.empty());
		when(planetRepositoryMock.save(planetArgumentCaptor.capture())).thenReturn(outPlanet);
		when(propertiesMock.getApplicationName()).thenReturn(applicationName);

		final var result = planetService.createPlanet(incomePlanet);

		assertEquals(outPlanet, result);

		final var argumentCaptorValue = planetArgumentCaptor.getValue();

		assertEquals(incomePlanet.getName(), argumentCaptorValue.getName());
		assertEquals(incomePlanet.getClimate(), argumentCaptorValue.getClimate());
		assertEquals(incomePlanet.getTerrain(), argumentCaptorValue.getTerrain());
		assertEquals(incomePlanet.getCreatedBy(), argumentCaptorValue.getCreatedBy());
		assertDoesNotThrow(() -> UUID.fromString(argumentCaptorValue.getUuid()));
		assertNotNull(argumentCaptorValue.getCreateAt());
		assertNull(argumentCaptorValue.getId());

		verify(planetRepositoryMock, times(1)).findByNameIgnoreCase(incomePlanet.getName());
		verify(planetRepositoryMock, times(1)).save(argumentCaptorValue);
		verify(propertiesMock, times(1)).getApplicationName();
	}

	@Test
	public void createPlanet_shouldNotCreateWhenNameAlreadyExists() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var incomePlanet = new Planet(planetName, planetClimate, planetTerrain);

		when(planetRepositoryMock.findByNameIgnoreCase(incomePlanet.getName())).thenReturn(Optional.of(new Planet()));

		final var assertThrows = assertThrows(ConflictException.class, () -> planetService.createPlanet(incomePlanet));
		assertEquals(String.format("Planet with name %s already exist", incomePlanet.getName()), assertThrows.getMessage());
		verify(planetRepositoryMock, times(1)).findByNameIgnoreCase(incomePlanet.getName());
	}

	@Test
	public void findPlanetsWithFilmAppearances_shouldFindWithNameParameter() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var foundPlanet1 = new Planet(planetId, UUID.randomUUID()
				.toString(), planetName, planetClimate, planetTerrain, LocalDateTime.now(), planetUser);

		final var filmAppearances = getRandomInteger();
		final var foundPlanetIntegration = new FoundPlanetIntegration(planetName, filmAppearances);
		when(planetRepositoryMock.findByNameIgnoreCase(planetName)).thenReturn(Optional.of(foundPlanet1));
		when(starWarsIntegrationMock.findOnePlanetByName(foundPlanet1.getName())).thenReturn(
				Optional.of(foundPlanetIntegration));

		final var result = planetService.findPlanetsWithFilmAppearances(planetName);

		assertEquals(1, result.size());
		result.forEach((key, value) -> {
			assertEquals(filmAppearances, value);
			assertEquals(foundPlanet1, key);
		});

		verify(planetRepositoryMock, times(1)).findByNameIgnoreCase(planetName);
		verify(starWarsIntegrationMock, times(1)).findOnePlanetByName(planetName);
	}

	@Test
	public void findPlanetsWithFilmAppearances_shouldFindWithoutNameParameter() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var planetName2 = getRandomString();
		final var planetTerrain2 = getRandomString();
		final var planetClimate2 = getRandomString();
		final var planetId2 = getRandomString();
		final var planetUser2 = getRandomString();

		final var foundPlanet1 = new Planet(planetId, UUID.randomUUID()
				.toString(), planetName, planetClimate, planetTerrain, LocalDateTime.now(), planetUser);

		final var foundPlanet2 = new Planet(planetId2, UUID.randomUUID()
				.toString(), planetName2, planetClimate2, planetTerrain2, LocalDateTime.now(), planetUser2);

		final var filmAppearances1 = getRandomInteger();
		final var foundPlanetIntegration1 = new FoundPlanetIntegration(planetName, filmAppearances1);

		final var filmAppearances2 = getRandomInteger();
		final var foundPlanetIntegration2 = new FoundPlanetIntegration(planetName2, filmAppearances2);
		when(planetRepositoryMock.findAll()).thenReturn(List.of(foundPlanet1, foundPlanet2));
		when(starWarsIntegrationMock.findOnePlanetByName(foundPlanet1.getName())).thenReturn(
				Optional.of(foundPlanetIntegration1));
		when(starWarsIntegrationMock.findOnePlanetByName(foundPlanet2.getName())).thenReturn(
				Optional.of(foundPlanetIntegration2));

		final var result = planetService.findPlanetsWithFilmAppearances(null);

		assertEquals(2, result.size());
		assertEquals(filmAppearances1, result.get(foundPlanet1));
		assertEquals(filmAppearances2, result.get(foundPlanet2));

		verify(planetRepositoryMock, times(0)).findByNameIgnoreCase(anyString());
		verify(planetRepositoryMock, times(1)).findAll();
		verify(starWarsIntegrationMock, times(1)).findOnePlanetByName(planetName);
		verify(starWarsIntegrationMock, times(1)).findOnePlanetByName(planetName2);
	}

	@Test
	public void findPlanetsWithFilmAppearances_shouldNotFoundAnyPlanet() {
		when(planetRepositoryMock.findAll()).thenReturn(Collections.emptyList());

		final var result = planetService.findPlanetsWithFilmAppearances(null);

		assertEquals(0, result.size());
		verify(planetRepositoryMock, times(0)).findByNameIgnoreCase(anyString());
		verify(starWarsIntegrationMock, times(0)).findOnePlanetByName(anyString());
		verify(planetRepositoryMock, times(1)).findAll();
	}

	@Test
	public void findPlanetWithFilmAppearancesById_shouldFind() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();
		final var uuid = UUID.randomUUID()
				.toString();

		final var foundPlanet = new Planet(planetId, uuid, planetName, planetClimate, planetTerrain, LocalDateTime.now(),
				planetUser);

		final var filmAppearances = getRandomInteger();
		final var foundPlanetIntegration = new FoundPlanetIntegration(planetName, filmAppearances);

		when(planetRepositoryMock.findByUuid(uuid)).thenReturn(Optional.of(foundPlanet));
		when(starWarsIntegrationMock.findOnePlanetByName(foundPlanet.getName())).thenReturn(
				Optional.of(foundPlanetIntegration));

		final var result = planetService.findPlanetWithFilmAppearancesById(uuid);

		assertEquals(foundPlanet, result.getKey());
		assertEquals(filmAppearances, result.getValue());

		verify(planetRepositoryMock, times(1)).findByUuid(uuid);
		verify(starWarsIntegrationMock, times(1)).findOnePlanetByName(planetName);
	}

	@Test
	public void findPlanetWithFilmAppearancesById_shouldNotFind() {
		final var uuid = UUID.randomUUID()
				.toString();
		when(planetRepositoryMock.findByUuid(uuid)).thenReturn(Optional.empty());

		final var assertThrows = assertThrows(DataNotFoundException.class,
				() -> planetService.findPlanetWithFilmAppearancesById(uuid));

		assertEquals("Planet not found", assertThrows.getMessage());

		verify(planetRepositoryMock, times(1)).findByUuid(uuid);
		verify(starWarsIntegrationMock, times(0)).findOnePlanetByName(any());
	}

	@Test
	public void deletePlanetById_shouldDelete() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();
		final var uuid = UUID.randomUUID()
				.toString();

		final var foundPlanet = new Planet(planetId, uuid, planetName, planetClimate, planetTerrain, LocalDateTime.now(),
				planetUser);

		when(planetRepositoryMock.findByUuid(uuid)).thenReturn(Optional.of(foundPlanet));
		doNothing().when(planetRepositoryMock)
				.delete(foundPlanet);

		planetService.deletePlanetById(uuid);

		verify(planetRepositoryMock, times(1)).findByUuid(uuid);
		verify(planetRepositoryMock, times(1)).delete(foundPlanet);
	}

	@Test
	public void deletePlanetById_shouldNotFound() {
		final var uuid = UUID.randomUUID()
				.toString();
		when(planetRepositoryMock.findByUuid(uuid)).thenReturn(Optional.empty());

		final var assertThrows = assertThrows(DataNotFoundException.class, () -> planetService.deletePlanetById(uuid));

		assertEquals("Planet not found", assertThrows.getMessage());

		verify(planetRepositoryMock, times(1)).findByUuid(uuid);
		verify(planetRepositoryMock, times(0)).delete(any());
	}
}