/*
 * Copyright(c) 2010 Les Zindeps.
 *
 * The code source of this project is distributed
 * under the Affero GPL GNU AFFERO GENERAL PUBLIC LICENSE
 * Version 3, 19 November 2007
 *
 * This file is part of project LesZindeps. The source code is
 * hosted on GitHub. The initial project was launched by
 * Nicolas Martignole.
 *
 * LesZindeps is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LesZindeps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * Please see COPYING.AGPL.txt for the full text license
 * or online http://www.gnu.org/licenses/agpl.html
 */

/**
 * Created by Nicolas Martignole.
 * User: nicolas
 * Date: 31/12/10
 * Time: 00:04
 */

function onLinkedInLoad() {
    // Listen for an auth event to occur
    IN.Event.on(IN, "auth", onLinkedInAuth);
}

function onLinkedInAuth() {
    // Send the linkedInId back to the server
    IN.API.Profile("me")
            .fields("id", "first-name", "last-name", "picture-url", "summary", "headline", "specialties")
            .result(updateProfile)
            .error(function(e) {
        alert("Impossible de s\'authentifier." + e);
    });
}

function updateProfile(profiles) {
    $("#mainPanel").show();

    var id = profiles.values[0].id;
    // AJAX call to pass back id to your server
    $('#resultLinkedIn').load('/admin/copyLinkedInProfile?linkedInId=' + id);

    // After they've signed-in, print a form to enable keyword searching
    var div = document.getElementById("mainPanel");

    div.innerHTML = '<h2>Vos données LinkedIn</h2>';
    div.innerHTML += '<P>Voici les données chargées à partir de LinkedIn pour votre profil :</p>';
    div.innerHTML += '<form action="/admin/doupdatemyprofilefromLinkedIn" method="post" enctype="application/x-www-form-urlencoded">' +
            'Prénom : <input id="firstName" name="firstName" size="70" value="' + profiles.values[0].firstName + '" type="text"><br/>' +
            'Nom : <input id="lastName" name="lastName" size="70" value="' + profiles.values[0].lastName + '" type="text"><br/>' +
            'Titre : <input id="title" name="title" size="70" value="' + profiles.values[0].headline + '" type="text"><br/>' +
            'Photo : <img src="' + profiles.values[0].pictureUrl + '" alt="image linkedIn"/><br/>' +
            '<input id="pictureUrl" name="pictureUrl" size="70" value="' + profiles.values[0].pictureUrl + '" type="hidden"><br/>' +
            'Bio : <br/><textarea id="bio" name="bio" rows="6" cols="70">' + profiles.values[0].summary + '</textarea><br/>' +
            'Technos : <br/><textarea id="techno" name="techno" rows="6" cols="70">' + profiles.values[0].specialties + '</textarea><br/>' +
            '<input type="submit" value="Mettre à jour mon profil Zindep" />';
    div.innerHTML += '</fieldset>';
    div.innerHTML += "<br/></br/>Si vous ne souhaitez pas mettre à jour votre profil vous pouvez <a href='javascript:logout()'>fermer votre session LinkedIn.</a>";
}

function logout() {
    IN.User.logout(logoutConfirmed);
    $("#mainPanel").hide();
}

function logoutConfirmed() {
}

