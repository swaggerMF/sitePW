import {Component} from '@angular/core';
import {bootstrapApplication} from '@angular/platform-browser';
@Component({selector:'app-root',template:'<h1>TripBoard</h1><p>Your next adventure starts here.</p>'})
class App {}
bootstrapApplication(App).catch(console.error);
