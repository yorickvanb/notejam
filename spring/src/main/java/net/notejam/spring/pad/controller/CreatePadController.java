package net.notejam.spring.pad.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import net.notejam.spring.URITemplates;
import net.notejam.spring.pad.Pad;
import net.notejam.spring.pad.PadService;
import net.notejam.spring.pad.controller.PadsAdvice.Pads;

/**
 * The create pad controller.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Controller
@RequestMapping(URITemplates.CREATE_PAD)
@PreAuthorize("isAuthenticated()")
@Pads
public class CreatePadController {

    @Autowired
    private PadService service;

    @ModelAttribute
    public Pad pad() {
        return service.buildPad();
    }

    /**
     * Shows the form for creating a pad.
     * 
     * @return The view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showCreatePadForm() {
        return "pad/create";
    }

    /**
     * Creates a new pad.
     * 
     * @return The view
     */
    @RequestMapping(method = RequestMethod.POST)
    public String createPad(@Valid Pad pad, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pad/create";
        }

        service.savePad(pad);

        return String.format("redirect:%s", buildCreatedPadUri(pad.getId()));
    }

    /**
     * Builds the URI for the created pad.
     * 
     * @param id
     *            The pad id
     * @return The URI
     */
    private String buildCreatedPadUri(int id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(URITemplates.VIEW_PAD);
        uriBuilder.queryParam("created");
        return uriBuilder.buildAndExpand(id).toUriString();
    }

}
