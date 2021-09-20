package com.main.videosapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.main.videosapi.entity.Category;
import com.main.videosapi.entity.Media;
import com.main.videosapi.entity.Partner;
import com.main.videosapi.entity.SubCategory;
import com.main.videosapi.entity.Teaser;
import com.main.videosapi.exception.NotFoundExpection;
import com.main.videosapi.repository.CategoryRepository;
import com.main.videosapi.repository.MediaRepository;
import com.main.videosapi.repository.PartnerRepository;
import com.main.videosapi.repository.SubCategoryRepository;
import com.main.videosapi.repository.TeaserRepository;
import com.main.videosapi.responses.JwtResponse;
import com.main.videosapi.responses.ResponseHandler;
import com.main.videosapi.service.MyUserDetailsService;
import com.main.videosapi.util.JwtUtils;

@RestController
public class ApiController {

	@Autowired
	CategoryRepository catRepository;

	@Autowired
	SubCategoryRepository subCatRepository;

	@Autowired
	MediaRepository mediaRepository;

	@Autowired
	TeaserRepository teaserRepository;

	@Autowired
	private PagedResourcesAssembler<Media> mediaAssembler;

	@Autowired
	private PagedResourcesAssembler<Teaser> teaserAssembler;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	MyUserDetailsService userDetails;

	@Autowired
	JwtUtils jwttoken;

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private PasswordEncoder passEncoder;

	@Autowired
	BCryptPasswordEncoder bycrpt;

	@PostMapping(path = "/Auth")
	public ResponseEntity<?> authenticatePartner(@Valid @RequestBody Partner partner) {
		Authentication authenticate = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(partner.getUsername(), partner.getPassword()));
		if (!authenticate.isAuthenticated())
			throw new BadCredentialsException("Bad Credentials");
		UserDetails user = userDetails.loadUserByUsername(partner.getUsername());
		String token = jwttoken.generateToken(user);
		return new ResponseEntity<Object>(new ResponseHandler(true, "", new JwtResponse(token)), HttpStatus.OK);

	}

	@PostMapping(path = "/mediaUpload/")
	public ResponseEntity<?> upload(@RequestParam MultipartFile file) throws IllegalStateException, IOException {
		String name = file.getOriginalFilename();
		file.transferTo(new File("D:\\Dumps\\" + name));
		return new ResponseEntity<Object>(new ResponseHandler(true, "", "uplaoded"), HttpStatus.OK);

	}

	@PostMapping(path = "/Partner/")
	public ResponseEntity<?> addPartner(@Valid @RequestBody Partner partner) throws Exception {

		Optional<Partner> checkUser = partnerRepository.findByUsername(partner.getUsername());

		if (checkUser.isPresent())
			throw new Exception("Username Already Exists - " + partner.getUsername());

		partner.setPassword(passEncoder.encode(partner.getPassword()));

		@Valid
		Partner user = partnerRepository.save(partner);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(uri).build();

	}

	@GetMapping(path = "/Partner/{id}/cats")
	public ResponseEntity<?> getPartnerCategorylist(@PathVariable int id, ModelMap model) throws Exception {

		Optional<Partner> user = partnerRepository.findById(id);
		if (!user.isPresent())
			throw new NotFoundExpection("id- " + id);

		model.addAttribute("list", user.get().getCategory());
		return new ResponseEntity<>(new ResponseHandler(true, "", model), HttpStatus.OK);

	}

	@GetMapping(path = "/cats/")
	public ResponseEntity<Object> showAllCategories(Pageable pagable, PagedResourcesAssembler<Category> catsAssembler) {
		PagedModel<EntityModel<Category>> pagedCats = catsAssembler.toModel(catRepository.findAll(pagable));
		return new ResponseEntity<>(new ResponseHandler(true, "", pagedCats), HttpStatus.OK);
	}

	@PostMapping(path = "/cats/")
	public ResponseEntity<Object> createCategory(@Valid @RequestBody Category category) {

		@Valid
		Category cat = catRepository.save(category);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cat.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping(path = "/cats/{id}")
	public ResponseEntity<Object> getCategory(@PathVariable int id) {
		Optional<Category> category = catRepository.findById(id);
		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);
		return new ResponseEntity<>(new ResponseHandler(true, "", category), HttpStatus.OK);
	}

	@PutMapping(path = "/cats/{id}")
	public ResponseEntity<Object> updateCategory(@PathVariable int id, @Valid @RequestBody Category category) {

		Optional<Category> cat = catRepository.findById(id);
		if (!cat.isPresent())
			throw new NotFoundExpection("id- " + id);

		category.setId(id);
		catRepository.save(category);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping(path = "/cats/{id}")
	public ResponseEntity<Object> deleteCategory(@PathVariable int id, ModelMap model) {

		Optional<Category> cat = catRepository.findById(id);
		if (!cat.isPresent())
			throw new NotFoundExpection("id- " + id);

		catRepository.deleteById(id);

		List<Category> allCat = catRepository.findAll();
		model.addAttribute("list", allCat);
		return new ResponseEntity<>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@PostMapping(path = "/cats/{id}/subcat")
	public ResponseEntity<Object> createCategory(@PathVariable int id, @Valid @RequestBody SubCategory subcat) {

		Optional<Category> category = catRepository.findById(id);
		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);

		subcat.setCategory(category.get());
		subCatRepository.save(subcat);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(subcat.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping(path = "/cats/{id}/subcat")
	public ResponseEntity<Object> getSubCategoryByCatID(@PathVariable int id, Pageable pageable,
			PagedResourcesAssembler<SubCategory> subCatAssembler) {

		Optional<Category> category = catRepository.findById(id);
		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);

		PagedModel<EntityModel<SubCategory>> pagedSubCategory = subCatAssembler
				.toModel(subCatRepository.findByCategory(category.get(), pageable));

		return new ResponseEntity<Object>(new ResponseHandler(true, "", pagedSubCategory), HttpStatus.OK);
	}

	@GetMapping(path = "/subcat/{id}")
	public ResponseEntity<Object> getSubCategory(@PathVariable int id) {
		Optional<SubCategory> category = subCatRepository.findById(id);
		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);
		return new ResponseEntity<>(new ResponseHandler(true, "", category), HttpStatus.OK);
	}

	@PutMapping(path = "/subcat/{id}")
	public ResponseEntity<Object> updateSubCategory(@PathVariable int id, @Valid @RequestBody SubCategory category) {

		Optional<SubCategory> cat = subCatRepository.findById(id);
		if (!cat.isPresent())
			throw new NotFoundExpection("id- " + id);

		category.setId(id);
		subCatRepository.save(category);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping(path = "/subcat/{id}")
	public ResponseEntity<Object> deleteSubCategory(@PathVariable int id, ModelMap model) {

		Optional<SubCategory> cat = subCatRepository.findById(id);
		if (!cat.isPresent())
			throw new NotFoundExpection("id- " + id);

		subCatRepository.deleteById(id);

		List<SubCategory> allCat = subCatRepository.findAll();
		model.addAttribute("list", allCat);
		return new ResponseEntity<>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@PostMapping(path = "/subcat/{id}/media")
	public ResponseEntity<Object> createMedia(@PathVariable int id, @Valid @RequestBody Media media) {

		Optional<SubCategory> subcategory = subCatRepository.findById(id);

		if (!subcategory.isPresent())
			throw new NotFoundExpection("id- " + id);

		media.setSubcategory(subcategory.get());
		media.setCategory(subcategory.get().getCategory());

		mediaRepository.save(media);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(media.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping(path = "/cats/{id}/media")
	public ResponseEntity<Object> getMediaByCat(@PathVariable int id, ModelMap model, Pageable pageable) {

		Optional<Category> category = catRepository.findById(id);

		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);

		PagedModel<EntityModel<Media>> pagedMedia = mediaAssembler
				.toModel(mediaRepository.findByCategory(category.get(), pageable));
		model.addAttribute("list", pagedMedia);

		return new ResponseEntity<Object>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@GetMapping(path = "/subcat/{id}/media")
	public ResponseEntity<Object> getMediaBySubCat(@PathVariable int id, ModelMap model, Pageable pageable) {

		Optional<SubCategory> category = subCatRepository.findById(id);

		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);

		PagedModel<EntityModel<Media>> pagedMedia = mediaAssembler
				.toModel(mediaRepository.findBySubcategory(category.get(), pageable));
		model.addAttribute("list", pagedMedia);

		return new ResponseEntity<Object>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@GetMapping(path = "/media/")
	public ResponseEntity<Object> getAllMedia(ModelMap model, Pageable pageable) {

		PagedModel<EntityModel<Media>> pagedMedia = mediaAssembler.toModel(mediaRepository.findAll(pageable));
		model.addAttribute("list", pagedMedia);

		return new ResponseEntity<Object>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@GetMapping(path = "/media/{id}")
	public ResponseEntity<Object> getMediaByID(@PathVariable int id, Pageable pageable, ModelMap model) {
		Optional<Media> media = mediaRepository.findById(id);
		if (!media.isPresent())
			throw new NotFoundExpection("id- " + id);
		EntityModel<Optional<Media>> entityModel = EntityModel.of(media);
		WebMvcLinkBuilder linkToMedia = linkTo(methodOn(this.getClass()).getAllMedia(model, pageable));
		entityModel.add(linkToMedia.withRel("all-media"));
		return new ResponseEntity<>(new ResponseHandler(true, "", entityModel), HttpStatus.OK);
	}

	@PutMapping(path = "/media/{id}")
	public ResponseEntity<Object> updateMedia(@PathVariable int id, @Valid @RequestBody Media media) {

		Optional<Media> mediaObj = mediaRepository.findById(id);
		if (!mediaObj.isPresent())
			throw new NotFoundExpection("id- " + id);

		media.setId(id);
		mediaRepository.save(media);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping(path = "/media/{id}")
	public ResponseEntity<Object> deleteMedia(@PathVariable int id, ModelMap model, Pageable pageable) {

		Optional<Media> mediaObj = mediaRepository.findById(id);
		if (!mediaObj.isPresent())
			throw new NotFoundExpection("id- " + id);

		mediaRepository.deleteById(id);
		PagedModel<EntityModel<Media>> pagedMedia = mediaAssembler.toModel(mediaRepository.findAll(pageable));
		model.addAttribute("list", pagedMedia);

		return new ResponseEntity<>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@GetMapping(path = "/subcat/{id}/teaser")
	public ResponseEntity<Object> getTeaserBySubCat(@PathVariable int id, ModelMap model, Pageable pageable) {

		Optional<SubCategory> category = subCatRepository.findById(id);

		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);

		PagedModel<EntityModel<Teaser>> pagedTeaser = teaserAssembler
				.toModel(teaserRepository.findBySubCategory(category.get(), pageable));
		model.addAttribute("list", pagedTeaser);

		return new ResponseEntity<Object>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@GetMapping(path = "/cats/{id}/teaser")
	public ResponseEntity<Object> getTeaserByCat(@PathVariable int id, ModelMap model, Pageable pageable) {

		Optional<Category> category = catRepository.findById(id);

		if (!category.isPresent())
			throw new NotFoundExpection("id- " + id);

		PagedModel<EntityModel<Teaser>> pagedTeaser = teaserAssembler
				.toModel(teaserRepository.findByCategory(category.get(), pageable));
		model.addAttribute("list", pagedTeaser);

		return new ResponseEntity<Object>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@PostMapping(path = "/subcat/{id}/teaser")
	public ResponseEntity<Object> createTeaser(@PathVariable int id, @Valid @RequestBody Teaser teaser) {

		Optional<SubCategory> subcategory = subCatRepository.findById(id);

		if (!subcategory.isPresent())
			throw new NotFoundExpection("id- " + id);

		teaser.setSubCategory(subcategory.get());
		teaser.setCategory(subcategory.get().getCategory());

		teaserRepository.save(teaser);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(teaser.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping(path = "/teaser/")
	public ResponseEntity<Object> getAllTeaser(ModelMap model, Pageable pageable) {

		PagedModel<EntityModel<Teaser>> pagedTeaser = teaserAssembler.toModel(teaserRepository.findAll(pageable));
		model.addAttribute("list", pagedTeaser);

		return new ResponseEntity<Object>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

	@GetMapping(path = "/teaser/{id}")
	public ResponseEntity<Object> getTeaserByID(@PathVariable int id, Pageable pageable, ModelMap model) {
		Optional<Teaser> teaser = teaserRepository.findById(id);
		if (!teaser.isPresent())
			throw new NotFoundExpection("id- " + id);
		EntityModel<Optional<Teaser>> entityModel = EntityModel.of(teaser);
		WebMvcLinkBuilder linkToTeaser = linkTo(methodOn(this.getClass()).getAllTeaser(model, pageable));
		entityModel.add(linkToTeaser.withRel("all-teaser"));
		return new ResponseEntity<>(new ResponseHandler(true, "", entityModel), HttpStatus.OK);
	}

	@PutMapping(path = "/teaser/{id}")
	public ResponseEntity<Object> updateTeaser(@PathVariable int id, @Valid @RequestBody Teaser teaser) {

		Optional<Teaser> teaserObj = teaserRepository.findById(id);
		if (!teaserObj.isPresent())
			throw new NotFoundExpection("id- " + id);

		teaser.setId(id);
		teaserRepository.save(teaser);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping(path = "/teaser/{id}")
	public ResponseEntity<Object> deleteTeaser(@PathVariable int id, ModelMap model, Pageable pageable) {

		Optional<Teaser> teaserObj = teaserRepository.findById(id);
		if (!teaserObj.isPresent())
			throw new NotFoundExpection("id- " + id);

		teaserRepository.deleteById(id);
		PagedModel<EntityModel<Teaser>> pagedTeaser = teaserAssembler.toModel(teaserRepository.findAll(pageable));
		model.addAttribute("list", pagedTeaser);

		return new ResponseEntity<>(new ResponseHandler(true, "", model), HttpStatus.OK);
	}

}
