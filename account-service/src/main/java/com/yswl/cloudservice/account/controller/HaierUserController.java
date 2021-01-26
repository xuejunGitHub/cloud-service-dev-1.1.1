package com.yswl.cloudservice.account.controller;

import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.HaierUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @since 2020/12/14-下午5:24
 */
@RestController
@RequestMapping("haierUser")
public class HaierUserController {

	@Autowired
	private HaierUserService userService;

	@PostMapping("upload")
	public Object uploadFile(UserQuery query, MultipartFile file) {
		return userService.uploadFile(file, query);
	}

}
