import * as React from "react";
import "../public/fonts/fonts.css";
import Router from "next/router";
import { ApplicationProvider } from "../utils/application";
import { DashboardProvider } from "../utils/dashboard";
import { ConsumerProcessProvider } from "../utils/consumerProcess";
import { MetricProcessProvider } from "../utils/metricProcess";
import { ReferentialProcessProvider } from "../utils/referential";
import { SimulationProvider } from "../utils/simulation";
import { ProcessProvider } from "../utils/process";
import { MetricProvider } from "../utils/metric";
import { LogstashConfigurationProvider } from "../utils/logstashConfiguration";
// import { LogstashConfigurationProvider } from "../utils/logstashConfiguration";

// export function redirectUser(ctx, location) {
//   if (ctx.req) {
//     ctx.res.writeHead(302, { Location: location });
//     ctx.res.end();
//   } else {
//     Router.push(location);
//   }
// }

export default ({ Component, pageProps }) => (
  <ApplicationProvider>
    <ConsumerProcessProvider>
      <MetricProcessProvider>
        <ReferentialProcessProvider>
          <SimulationProvider>
            <ProcessProvider>
              <MetricProvider>
                <LogstashConfigurationProvider>
                  <Component {...pageProps} />
                </LogstashConfigurationProvider>
              </MetricProvider>
            </ProcessProvider>
          </SimulationProvider>
        </ReferentialProcessProvider>
      </MetricProcessProvider>
    </ConsumerProcessProvider>
  </ApplicationProvider>
);
