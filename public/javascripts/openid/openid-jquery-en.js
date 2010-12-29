/*
	Simple OpenID Plugin
	http://code.google.com/p/openid-selector/
	
	This code is licenced under the New BSD License.
*/

var providers_large = {
	google : {
		name : 'Google',
		url : 'https://www.google.com/accounts/o8/id'
	},
	yahoo : {
		name : 'Yahoo',
		url : 'http://me.yahoo.com/'
	},
	aol : {
		name : 'AOL',
		label : 'Entrer votre AOL screenname :',
		url : 'http://openid.aol.com/{username}'
	},
	myopenid : {
		name : 'MyOpenID',
		label : 'Entrer votre nom d\'utilisateur MyOpenID :',
		url : 'http://{username}.myopenid.com/'
	},
	openid : {
		name : 'OpenID',
		label : 'Entrer votre URL Open ID :',
		url : null
	}
};

var providers_small = {
	/*livejournal : {
		name : 'LiveJournal',
		label : 'Entrer votre Livejournal username.',
		url : 'http://{username}.livejournal.com/'
	},
	*/
	/* flickr: {
	    name: 'Flickr',        
	    label: 'Enter your Flickr username.',
	    url: 'http://flickr.com/{username}/'
	}, */
	/* technorati: {
	    name: 'Technorati',
	    label: 'Enter your Technorati username.',
	    url: 'http://technorati.com/people/technorati/{username}/'
	}, */
	/*wordpress : {
		name : 'Wordpress',
		label : 'Enter your Wordpress.com username.',
		url : 'http://{username}.wordpress.com/'
	},
	blogger : {
		name : 'Blogger',
		label : 'Your Blogger account',
		url : 'http://{username}.blogspot.com/'
	}, */
	/*verisign : {
		name : 'Verisign',
		label : 'Your Verisign username',
		url : 'http://{username}.pip.verisignlabs.com/'
	},
	*/
	/* vidoop: {
	    name: 'Vidoop',
	    label: 'Your Vidoop username',
	    url: 'http://{username}.myvidoop.com/'
	}, */
	/* launchpad: {
	    name: 'Launchpad',
	    label: 'Your Launchpad username',
	    url: 'https://launchpad.net/~{username}'
	}, */
	/*claimid : {
		name : 'ClaimID',
		label : 'Votre ClaimID username',
		url : 'http://claimid.com/{username}'
	}, */
	/*clickpass : {
		name : 'ClickPass',
		label : 'Enter your ClickPass username',
		url : 'http://clickpass.com/public/{username}'
	},*/
	/*google_profile : {
		name : 'Google Profile',
		label : 'Entrer le nom de votre Google Profile ',
		url : 'http://www.google.com/profiles/{username}'
	}*/
};

openid.lang = 'en';
openid.demo_text = 'In client demo mode. Normally would have submitted OpenID:';
openid.signin_text = 'S\'authentifier';
openid.image_title = 'se connecter avec {provider}';
