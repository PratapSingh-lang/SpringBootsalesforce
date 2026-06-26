import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { config } from './app/app.config.server';

import { provideServerRendering } from '@angular/platform-server';

const bootstrap = () => bootstrapApplication(App, config);

export default bootstrap;

export function bootstrap() {
  return bootstrapApplication(App, {
    providers: [
      provideServerRendering(), // provide SSR platform services
    ],
  });
}
