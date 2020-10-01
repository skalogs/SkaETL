import React from 'react'
import Layout from '../components/common/Layout'
import ConsumerProcess from '../components/Dashboard/ConsumerProcess'
import MetricProcess from '../components/Dashboard/MetricProcess'
import TopSection from '../components/Dashboard/TopSection'
import TrafficProcess from '../components/Dashboard/TrafficProcess'
import { DashboardProvider } from '../utils/dashboard'

export default () => {
    return (
        <DashboardProvider>
            <Layout>
                <TopSection />
                <TrafficProcess />
                <ConsumerProcess />
                <MetricProcess />
            </Layout>
        </DashboardProvider>

    )
}

// export async function getStaticProps() {
//     // Call an external API endpoint to get posts.
//     // You can use any data fetching library
//     const resHome = await fetch('https://localhost:8000/home/fetch')
//     const home = await resHome.json()

//     const resDataCapture = await fetch('https://localhost:8000/home/dataCapture')
//     const dataCapture = await resDataCapture.json()

//     const resListProcess = await fetch('https://localhost:8000/process/findAll')
//     const listProcess = await resListProcess.json()

//     const resListMetricProcess = await fetch('https://localhost:8000/metric/listProcess')
//     const listMetricProcess = await resListMetricProcess.json()

//     // By returning { props: posts }, the Blog component
//     // will receive `posts` as a prop at build time
//     return {
//       props: {
//         home,
//         dataCapture,
//         listProcess,
//         listMetricProcess,
//       },
//     }
//   }
