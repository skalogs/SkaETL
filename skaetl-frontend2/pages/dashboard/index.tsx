import React from 'react'
import Layout from '../../components/common/Layout'
import ConsumerProcess from '../../components/Dashboard/ConsumerProcess'
import MetricProcess from '../../components/Dashboard/MetricProcess'
import TopSection from '../../components/Dashboard/TopSection'
import TrafficProcess from '../../components/Dashboard/TrafficProcess'

const Dashboard = () => {
    return (
        <Layout>
            <TopSection />
            <TrafficProcess />
            <ConsumerProcess />
            <MetricProcess />
        </Layout>
    )
}

export default Dashboard
