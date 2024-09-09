package com.sails.client_connect.controller;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.LeadService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/leads")
public class LeadController {

    private final LeadService leadService;

    /**
     * this end point ensures to create lead by the user
     *
     * @param leadDTO
     * @param session Taking User id from the Http session and set that user id in LeadDTO class
     *                leadService.createLead(leadDTO): it inserts the data (abstracted from the response body) with user id into lead table
     * @return return response with message,status code,and data which is passed in response body
     */
    @PostMapping("/create") //this end point ensures to create lead by the user
    public ResponseEntity<ApiResponse<LeadDTO>> createLead(
            @Valid @RequestBody LeadDTO leadDTO,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");


        leadDTO.setUserId(userId);

        LeadDTO createdLead = leadService.createLead(leadDTO);
        ApiResponse<LeadDTO> response = new ApiResponse<>(
                "Lead created successfully",
                HttpStatus.CREATED,
                createdLead
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @param id      Lead ID
     * @param session Get User ID from Session
     *                Get specific lead using ID
     * @return Lead that matches the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeadDTO> getLeadById(
            @PathVariable Long id,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        LeadDTO leadDTO = leadService.getLeadByIdAndUserId(id, userId);
        return new ResponseEntity<>(leadDTO, HttpStatus.OK);
    }

    /**
     * @param session Get User ID from Session
     *                Get all leads for the currently logged-in user
     * @return List of leads associated with the user
     */
    @GetMapping
    public ResponseEntity<List<LeadDTO>> getAllLeads(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        List<LeadDTO> leads = leadService.getAllLeadsByUserId(userId);
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }

    /**
     * @param id      Lead ID
     * @param leadDTO Values to update
     * @param session Get User ID from Session
     *                To update a specific lead using lead ID
     * @return Updated lead with a success message and HTTP status code
     */
    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LeadDTO>> updateLead(
            @PathVariable Long id,
            @RequestBody LeadDTO leadDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");
        leadDTO.setUserId(userId);

        LeadDTO updatedLead = leadService.updateLead(id, leadDTO);
        ApiResponse<LeadDTO> response = new ApiResponse<>(
                "Lead updated successfully",
                HttpStatus.OK,
                updatedLead
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param id      Lead ID
     * @param session Get User ID from Session
     *                Delete specific lead using ID
     * @return Success message indicating the lead was deleted with HTTP status code
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLead(
            @PathVariable Long id,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        leadService.deleteLead(id, userId);
        ApiResponse<Void> response = new ApiResponse<>(
                "Lead deleted successfully",
                HttpStatus.OK,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


