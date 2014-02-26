NOTE: not in development anymore. :(
====================================

Lacuna-Android-Client
=====================

Android client for The Lacuna Expanse. Source code for the project is contained in the 'Lacuna Expanse' directory.

Authors & Contributors
======================

	-The Vasari! (nmccallum7@gmail.com)

Instructions for setting up Eclipse:
====================================

1) Follow the following Youtube videos for setting up Eclipse and the Android Emulator:

	A) http://www.youtube.com/watch?v=SUOWNXGRc6g
		i) Link to the site Travis speaks of at the end of the video: http://www.mybringback.com/tutorial-series/441/setting-up-jre-on-you-computer-so-you-can-begin-java-programming/
	B) http://www.youtube.com/watch?v=857zrsYZKGo&feature=fvwrel
	C) http://www.youtube.com/watch?v=Da1jlmwuW_w&feature=fvwrel
	(Please make sure to download the latest version of of everything except for the Android platforms/packages)

2) Download and open the project using EGit:

	A) Right click in the empty area that should be the package manager and hit import.
	B) Open the 'Git' item tree, select 'Projects from Git', and hit 'next'
	C) Double click 'URI'
	D) Then in the 'URI' box type 'http://github.com/TheVasari/Lacuna-Android-Client.git' (Or the URL to which ever repo your trying to get into, the '.git' at the end of the URL is required). The 'Host:' and 'Repository Path:' boxes will automatically fill themselves as you fill in the 'URI' box.
	E) I've ignored the Connection section of this window, but I guess selecting 'HTTPS' in the 'Protocol' selector and typing '80' in the Port box can't do any harm ;)
	F) In the Authentication section type in your Github username and password. I don't understand what 'Store in Secure Store' does...

	G) Now hit next, EGit will now gather information about the branches in the repo.
	H) Now simply select all of the branches and click next.

	I) Now in the Directory section next select where you want the repo to de download to, the rest of the setting can be left alone.
	J) EGit will now download the project, this may or may nto take a while...

	K) Then at the top of this window select 'Import existing projects', hit next.
	L) Select project named 'Lacuna Expanse' and 'Library', hit finish. You now have the Lacuna-Android-Client project loaded into your Eclipse set up!

Overview of using EGit:
=======================

1) Committing and Pushing to the online repo:

	A) Once you have made the desired changes to the project right click on the whole projects folder in the Package Explorer, select Team > Commit.
	B) In the 'Commit Message' box type a brief overview of the changes you made. This should not be more than 2 sentences.
	C) The 'Author' and 'Commiter' boxes should be already be filled. If not, in both of them type '[Git account name] [Email address in angle brackets]'. E.G. 'TheVasari <thevasari@gmail.com>'
	D) Then select all the files in the bottom box.
	E) Hit 'Commit'.

	F) But wait, that's not all, next you must right click on the project and go into the Team menu again, then Remote, and click 'Push...'
	G) The 'Configured Remote Repository' should be correct, so select that and hit next.
	H) Now, in the 'Source Ref' dropdown box select 'master [branch]' Which will the change to something like 'refs/heads/master'
	I) Now across from that hit 'Add Spec' and hit next.
	J) EGit will do some checking, then all you need to do is hit 'Finish'.
	K) A window will appear on the screen displaying a progress bar, that is EGit pushing to the online repo. Once that completes It will display what it did, simply hit 'Ok'.
	L) And you're finished!
