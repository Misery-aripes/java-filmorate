package ru.yandex.practicum.filmorate.controllerTest;

/*
class FilmControllerTest {
    private FilmController filmController;
    private UserService userService; // Добавляем как поле
    private InMemoryUserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();

        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        userService = new UserService(userStorage); // Используем общий userStorage
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void createFilmShouldAddFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        var response = filmController.createFilm(film);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Inception", response.getBody().getName());
    }


    @Test
    void getAllFilmsShouldReturnEmptyListInitially() {
        var response = filmController.getAllFilms();

        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllFilmsShouldReturnListOfFilms() {
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);

        Film film2 = new Film();
        film2.setName("Interstellar");
        film2.setDescription("Space exploration epic");
        film2.setReleaseDate(LocalDate.of(2014, 11, 7));
        film2.setDuration(169);

        filmController.createFilm(film1);
        filmController.createFilm(film2);

        var response = filmController.getAllFilms();

        assertEquals(2, response.getBody().size());
    }

    @Test
    void getFilmByIdShouldReturnFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        var createdFilm = filmController.createFilm(film).getBody();

        var response = filmController.getFilmById(createdFilm.getId());

        assertNotNull(response.getBody());
        assertEquals("Inception", response.getBody().getName());
    }

    @Test
    void getFilmByIdShouldThrowExceptionIfNotFound() {
        assertThrows(Exception.class, () -> filmController.getFilmById(99));
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        var createdFilm = filmController.createFilm(film).getBody();

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getId());
        updatedFilm.setName("Interstellar");
        updatedFilm.setDescription("Space exploration epic");
        updatedFilm.setReleaseDate(LocalDate.of(2014, 11, 7));
        updatedFilm.setDuration(169);

        var response = filmController.updateFilm(updatedFilm);

        assertEquals("Interstellar", response.getBody().getName());
    }

    @Test
    void updateFilmShouldThrowExceptionIfFilmNotFound() {
        Film film = new Film();
        film.setId(999);
        film.setName("Non-existent film");

        assertThrows(Exception.class, () -> filmController.updateFilm(film));
    }

    @Test
    void addLikeShouldIncrementLikes() {
        User user = new User();
        user.setName("Test User");
        user.setLogin("testuser");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userService.createUser(user); // ID генерируется автоматически

        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        var createdFilm = filmController.createFilm(film).getBody();

        filmController.addLike(createdFilm.getId(), createdUser.getId());

        var response = filmController.getFilmById(createdFilm.getId());

        assertNotNull(response.getBody());
        assertTrue(response.getBody().getLikes().contains(createdUser.getId()));
    }

    @Test
    void addLikeShouldThrowExceptionIfFilmNotFound() {
        assertThrows(Exception.class, () -> filmController.addLike(99, 1));
    }
}*/
