package com.se2.alert.controller;

import com.se2.alert.service.impl.SolrToMySQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/solrToMySQL")
public class SolrToMySQLController {

	@Autowired
	SolrToMySQLService solrToMySQLService;

	@GetMapping(path = "/pcn")
	public @ResponseBody String pcn() throws Exception {
		solrToMySQLService.reBuildPcn();
		return "Done";
	}

	@GetMapping(path = "/dml")
	public @ResponseBody String dml() throws Exception {
		solrToMySQLService.reBuildDml();
		return "Done";
	}

	@GetMapping(path = "/acq")
	public @ResponseBody String acq() throws Exception {
		solrToMySQLService.reBuildAcq();
		return "Done";
	}

	@GetMapping(path = "/gidep")
	public @ResponseBody String gidep() throws Exception {
		solrToMySQLService.reBuildGidep();
		return "Done";
	}
}