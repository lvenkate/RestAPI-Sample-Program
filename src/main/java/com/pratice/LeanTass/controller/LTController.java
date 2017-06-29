package com.pratice.LeanTass.controller;

import java.text.ParseException;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pratice.LeanTass.dao.LTProjectorDAO;

import com.pratice.LeanTass.model.Projector;
import com.pratice.LeanTass.model.Reservation;


@Controller
@RequestMapping("/leantaas")
public class LTController {

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String home() {
		return "home";
	}

	//List all the projectors
	//List available projectors
	//List next available projectors
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/projectors", method = RequestMethod.GET)
	public @ResponseBody JSONObject AvailableProjectors(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "interval", required = false) String interval) throws ParseException {

		LTProjectorDAO ltProjectorDAO = new LTProjectorDAO();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "507");
		jsonObject.put("response", "Exception while persisting");
		List<Projector> response = ltProjectorDAO.getProjectors(from, to, interval);
		System.out.println(from + " " + to + " " + interval );
		System.out.println(" The response is " + response);
		if (from != null && to != null) {
			if (response != null){
			jsonObject.put("status", "200 OK");
			jsonObject.put("response", response);
			}
			else{
				jsonObject.put("status", "2001 OK");
				jsonObject.put("response", "No Projector available at this time,Try again!");
			}

		} else {
			
			jsonObject.put("status", "5001");
			jsonObject.put("response", response);

		}
		return jsonObject;
	}

	
	//Reserve a projector
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/projector", method = RequestMethod.POST)
	public @ResponseBody JSONObject ReserveProjectors(@RequestBody Reservation reservation) throws ParseException {

		LTProjectorDAO ltProjectorDAO = new LTProjectorDAO();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", "Exception while persisting");

		if (reservation.getFrom() != null && reservation.getTo() != null && reservation.getTeam() != null) {
			Reservation rs = ltProjectorDAO.reserveProjector(reservation.getFrom(), reservation.getTo(),
					reservation.getTeam());
			if (rs != null) {
				jsonObject.put("status", "200 OK");
				jsonObject.put("response", rs);
			}
			else
			{
				jsonObject.put("status", "2001 OK");
				jsonObject.put("response", "Could not provide a reservation at this time");
			}
			return jsonObject;
		} else  {
			jsonObject.put("status", "5001");
			jsonObject.put("response", "Missing Parameters");
			return jsonObject;
		}

	}

   //Cancel the reservation
   @SuppressWarnings("unchecked")
	@RequestMapping(value = "/projector", method = RequestMethod.DELETE)
	public @ResponseBody JSONObject CancelProjectors(@RequestBody Reservation reservation) throws ParseException {
		LTProjectorDAO ltProjectorDAO = new LTProjectorDAO();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", "Exception while persisting");

		if (reservation.getFrom() != null && reservation.getTo() != null && reservation.getTeam() != null) {
			Boolean status = ltProjectorDAO.cancelProjector(reservation.getFrom(), reservation.getTo(),
					reservation.getTeam());

			if (status == true) {
				jsonObject.put("status", "200 OK");
				jsonObject.put("response", status);
			}
			else
			 {
				jsonObject.put("status", "200 OK");
				jsonObject.put("response", "Cancellation failed");
			}	
			return jsonObject;
		} else {
			jsonObject.put("status", "5001");
			jsonObject.put("response", "Missing Parameters");
			return jsonObject;
		}

	}
}
