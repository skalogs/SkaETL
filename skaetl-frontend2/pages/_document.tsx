import { ServerStyleSheets } from "@material-ui/core/styles"
import Document, { Head, Main, NextScript } from "next/document"
import * as React from "react"

export default class MyDocument extends Document {
    // eslint-disable-next-line @typescript-eslint/explicit-member-accessibility
    render() {
        return (
            <html lang="en">
                <Head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <link rel="icon" href="/static/images/favicon.ico" />
                    <link rel="manifest" href="/manifest.json" />
                    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600;700&family=Open+Sans:wght@300;400;600;700&display=swap" rel="stylesheet" />
                </Head>
                <body>
                    <Main />
                    <NextScript />
                </body>
            </html>
        )
    }
}

MyDocument.getInitialProps = async ctx => {
    const sheets = new ServerStyleSheets()
    const originalRenderPage = ctx.renderPage

    ctx.renderPage = () =>
        originalRenderPage({
            enhanceApp: App => props => sheets.collect(<App {...props} />),
        })

    const initialProps = await Document.getInitialProps(ctx)

    return {
        ...initialProps,
        styles: [...React.Children.toArray(initialProps.styles), sheets.getStyleElement()],
    }
}
