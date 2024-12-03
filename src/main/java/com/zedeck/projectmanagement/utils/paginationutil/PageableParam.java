/**
 * @author developers@Shared-Helpdesk
 *
 * 
 */
package com.zedeck.projectmanagement.utils.paginationutil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Melckzedeck James
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableParam {
	private String sortBy;
	private String sortDirection;
	private Integer size;
	private Integer first;

	List<SearchFieldsDto> searchFields;

	public PageableParam(Integer size, Integer first) {
		this.size = size;
		this.first = first;
	}

	public PageableParam(String sortBy, String sortDirection, Integer size, Integer first) {
		this.sortBy = sortBy;
		this.sortDirection = sortDirection;
		this.size = size;
		this.first = first;
	}

}
